package com.controller;

import com.common.JdbcUtils;
import com.fr.decision.service.authority.DecisionUserServiceProvider;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.bean.user.DepRoleBean;
import com.fr.decision.webservice.bean.user.UserDetailInfoBean;
import com.fr.decision.webservice.utils.DecisionServiceConstants;
import com.fr.decision.webservice.utils.DecisionServiceUtils;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.org.apache.commons.lang3.StringUtils;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.record.FilterRecord;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Controller("v1Common")
@RequestMapping(value = "/{svc}")
@LoginStatusChecker(required = false)
public class Common {
    private static final String SQL_FILENAME = "sqlmap.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public Common(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 실시간 현황 필터 저장
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public String filter(@RequestBody List<FilterRecord> params) {
        try {
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            for (FilterRecord record : params) {
                jdbcUtils.update("deleteFilter", record);

                if (record.getDataList() != null && record.getDataList().size() > 0) {
                    jdbcUtils.batchUpdate("insertFilter", record.getDataList());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        return "success";
    }

    /**
     * 실시간 현황 필터 전체 삭제
     *
     * @param page
     * @param req
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.DELETE)
    @ResponseBody
    public String filter(@RequestBody String page, HttpServletRequest req) {
        try {
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
            paramMap.put("page", page);

            jdbcUtils.update("deleteFilter", paramMap);

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        return "success";
    }

    /**
     * 부서/직무/역할/조회가능채널 조회
     *
     * @param req
     * @param page
     * @return result (Map)
     *         result.customRoleNames (Array)
     *         result.departmentId    (String)
     *         result.departmentNames (Array)
     *         result.departmentPosts (Array)
     *         result.displayName     (String)
     *         result.fullPath        (String)
     *         result.postNames       (Array)
     *         result.channels        (Array)
     * @throws Exception
     */
    @RequestMapping(value = "/depAndRoles", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> depAndRoles(HttpServletRequest req, @RequestParam(value = "page", required = false) String page) throws Exception {
        // 부서/직무/역할 조회
        DecisionUserServiceProvider userServiceProvider = serviceContext.getService(DecisionUserServiceProvider.class);

        String userName = DecisionServiceUtils.getUserNameFromRequestCookie(req);

        UserDetailInfoBean userDetailInfoBean = userServiceProvider.getUserDetailInfoByUsername(userName);
        Map<String, Object> result = new ObjectMapper().convertValue(userDetailInfoBean, Map.class);

        List<DepRoleBean> depRoles = userServiceProvider.getUserDepAndCustomRoles(userName).getDepRoles();
        String departmentId = "";
        String fullPath = "";

        if (depRoles != null && !depRoles.isEmpty()) {
            DepRoleBean DepRoleBean = depRoles.get(0);
            departmentId = DepRoleBean.getDepartmentId();
            fullPath = StringUtils.join(DepRoleBean.getParentIds(), DecisionServiceConstants.FULL_PATH_SPLITTER);
        }

        result.put("departmentId", departmentId); // 소속 조직코드
        result.put("fullPath", fullPath);         // 소속 조직코드 fullpath

        // 조회 가능한 조직 정보
        if ("AGENT_POS".equals(page)) {
            setDamdang(result); // 매장
        } else {
            setChannel(result); // 모바일,홈,기업
        }

        return result;
    }

    /**
     * 조회 가능한 조직 조회
     *
     * >> 모바일,홈,기업 - 채널 기준
     * default) 본인 소속 채널
     * exc 1) 채널명과 동일한 역할이 부여된 계정은 해당 채널도 조회 가능
     * exc 2) SearchMaster 역할이 부여된 계정은 전체 조회 가능
     */
    public void setChannel(Map<String, Object> result) throws Exception {
        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        // 역할 체크
        List<String> roles = result.get("customRoleNames") != null ? (List<String>) result.get("customRoleNames") : null;
        List<Map<String, Object>> masterList = jdbcUtils.queryForList("selectMasterRole", null);
        List<String> master = new ArrayList(masterList.stream().map(e -> e.get("comm_value")).collect(Collectors.toList()));
        boolean isMaster = false;
        for (String role : roles) {
            if (master.contains(role)) {
                isMaster = true;
                break;
            }
        }

        // 모든 채널
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("depth", 1);
        List<Map<String, Object>> channelInfo = jdbcUtils.queryForList("selectOrgInfo", paramMap);

        // 소속 채널
        String myChannel = "";
        if (result.get("departmentId") != null && !"".equals(result.get("departmentId"))) {
            paramMap = new HashMap<>();
            paramMap.put("orgCd", result.get("departmentId"));
            Map<String, Object> myOrg = jdbcUtils.queryForMap("selectOrgInfo", paramMap);

            if (myOrg != null) {
                myChannel = String.valueOf(myOrg.get("channel_nm"));
            }
        }

        // 조회 가능 채널
        List<Map<String, Object>> channels = new ArrayList<>();
        for (Map<String, Object> c : channelInfo) {
            String chnName = (String) c.get("org_name");
            // 본인 소속 채널 or 조회 권한이 있는 채널 or 전체 조회 권한인 경우
            if (chnName.equals(myChannel) || roles.contains(chnName) || isMaster) {
                channels.add(c);
            }
        }

        result.put("myChannel", myChannel);
        result.put("channels", channels);
    }

    /**
     * 조회 가능한 조직 조회
     *
     * >> 매장 - 담당 기준
     * default) 본인 소속 담당
     * exc 1) 담당명과 동일한 역할이 부여된 계정은 해당 담당도 조회 가능
     * exc 2) SearchMaster 역할이 부여된 계정은 전체 조회 가능
     */
    public void setDamdang(Map<String, Object> result) throws Exception {
        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        // 역할 체크
        List<String> roles = result.get("customRoleNames") != null ? (List<String>) result.get("customRoleNames") : null;
        List<Map<String, Object>> masterList = jdbcUtils.queryForList("selectMasterRole", null);
        List<String> master = new ArrayList(masterList.stream().map(e -> e.get("comm_value")).collect(Collectors.toList()));
        boolean isMaster = false;
        for (String role : roles) {
            if (master.contains(role)) {
                isMaster = true;
                break;
            }
        }

        // 모든 담당
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("depth", 1);
        List<Map<String, Object>> damdangInfo = jdbcUtils.queryForList("selectOrgInfoPos", paramMap);

        // 소속 담당
        String myDamdang = "";
        if (result.get("departmentPosts") != null) {
            List<Map<String, String>> depPosts = (List<Map<String, String>>) result.get("departmentPosts");
            if (depPosts.size() > 0) {
                paramMap = new HashMap<>();
                paramMap.put("departments", depPosts.get(0).get("departments"));
                Map<String, Object> myOrg = jdbcUtils.queryForMap("selectOrgInfoPos", paramMap);

                if (myOrg != null) {
                    myDamdang = String.valueOf(myOrg.get("damdang_cd"));
                }
            }
        }

        // 조회 가능 담당
        List<Map<String, Object>> damdangs = new ArrayList<>();
        for (Map<String, Object> d : damdangInfo) {
            String damCd = (String) d.get("org_cd");
            String damNm = (String) d.get("org_name");
            // 본인 소속 담당 or 전체 조회 권한인 경우
            if (damCd.equals(myDamdang) || roles.contains(damNm) || isMaster) {
                damdangs.add(d);
            }
        }

        result.put("myDamdang", myDamdang);
        result.put("damdangs", damdangs);
    }

    /**
     * 조회 가능한 조직 조회
     *
     * @param req
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getChannelByAuth(HttpServletRequest req) throws Exception {
        Map<String, Object> depAndRoles = this.depAndRoles(req, "");

        return (List<Map<String, Object>>) depAndRoles.get("channels");
    }
    public List<Map<String, Object>> getDamdangByAuth(HttpServletRequest req) throws Exception {
        Map<String, Object> depAndRoles = this.depAndRoles(req, "AGENT_POS");

        return (List<Map<String, Object>>) depAndRoles.get("damdangs");
    }

    /**
     * get winker properties
     *
     * @return
     */
    @RequestMapping(value = "/wprop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> winkerProperties() {
        Map<String, Object> result = new HashMap<>();
        Properties props = getProperties("conf/app.properties");

        result.put("serverType", props.getProperty("server.type"));
        result.put("strHostP", props.getProperty("winker.primary.host"));
        result.put("intPortP", props.getProperty("winker.primary.port"));
        result.put("strHostB", props.getProperty("winker.backup.host"));
        result.put("intPortB", props.getProperty("winker.backup.port"));

        return result;
    }

    /*
        get properties
        로컬 환경: 프로젝트폴더\build\classes 하위
    */
    public static Properties getProperties(String path) {
        Properties props = new Properties();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) cl = ClassLoader.getSystemClassLoader();

            URL url = cl.getResource(path);

            if (url == null) {
                throw new Exception("properties not exist. path=" + path);
            }

            String filepath = url.getFile();
            props.load(new InputStreamReader(new FileInputStream(filepath)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }

    /*
        get property in path
    */
    public static String getProperty(String path, String name) {
        String result = "";

        try {
            Properties props = getProperties(path);
            result = props.getProperty(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}