package com.controller.v2;

import com.common.JdbcUtils;
import com.common.WebUtil;
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
import com.fr.third.springframework.http.MediaType;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.record.MultiFilterDataRecord;
import com.record.MultiFilterRecord;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/{svc}/v2")
@LoginStatusChecker(required = false)
public class Common {
    private static final String SQL_FILENAME = "sqlmap_v2.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public Common(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 큐/스킬 현황 - 컨테이너 (큐, 스킬 각 영역 html 별도 분리)
     *
     * @param req
     * @param type
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/container", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String container(HttpServletRequest req, @RequestParam(value = "type", required = false) String type, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("type", type);

        return WebUtil.parseWebPageResourceSafe("/html/v2/common/container.html", result, svc);
    }

    /**
     * 팝업 전체 조회 (상담사현황, 큐모니터, 라우트현황)
     *
     * @param req
     * @param svc
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/popup/monitor", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String popupMonitor(HttpServletRequest req, @PathVariable("svc") String svc, @RequestParam(value = "type", required = false) String type) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 채널
        Map<String, Object> depAndRoles = new Common(serviceContext).depAndRoles(req, null);
        String myChannel = "";
        if (depAndRoles.get("myChannel") != null) {
            myChannel = (String) depAndRoles.get("myChannel");
        }
        List<Map<String, Object>> channelInfo = new ArrayList<>();
        if (depAndRoles.get("channels") != null) {
            channelInfo = (List<Map<String, Object>>) depAndRoles.get("channels");
        }

        result.put("departmentId", depAndRoles.get("departmentId"));
        result.put("customRoleNames", gson.toJson(depAndRoles.get("customRoleNames")));
        result.put("popupMaxCount", gson.toJson(depAndRoles.get("popupMaxCount")));
        result.put("myChannel", myChannel);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 역할에 따른 조회 권한 (별도로 부여된 권한이 없는 경우, 소속 팀 조회만 가능)
        paramMap.put("grp_nm", "realtime");
        paramMap.put("use_yn", "Y");
        result.put("viewLevels", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        // 조직, 상담원, 직책 정보
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));
        result.put("position", gson.toJson(jdbcUtils.queryForList("selectPosition", paramMap)));

        // 큐정보
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        // 팝업정보 (type, url, 기본사이즈 ...)
        paramMap.put("grp_nm", "realtime_col_type");
        paramMap.put("param5", "Y");
        List<Map<String, Object>> popupInfo = jdbcUtils.queryForList("selectComm", paramMap);
        result.put("popupInfo", gson.toJson(popupInfo));

        // 기존 저장된 팝업 필터 조회
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("types", Arrays.asList(popupInfo.stream().map(p -> p.get("comm_value")).toArray()));
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        // 팝업모니터링 레이아웃저장 데이터 조회
        paramMap = new HashMap<>();
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "POPUP");
        result.put("layoutData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("layoutValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        // 새창 여부
        result.put("type", type);

        return WebUtil.parseWebPageResourceSafe("/html/v2/common/popup_monitor.html", result, svc);
    }

    /**
     * 엑셀 요청 사유 입력 폼
     *
     * @param req
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/export/reason", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String exportReasonForm(HttpServletRequest req, @RequestParam(value = "f", required = false) String f, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("f", f);

        return WebUtil.parseWebPageResourceSafe("/html/v2/common/export_reason_form.html", result, svc);
    }

    /**
     * 엑셀 요청 사유 입력
     *
     * @param req
     * @param params
     * @return
     */
    @RequestMapping(value = "/export/reason", method = RequestMethod.POST)
    @ResponseBody
    public Boolean exportReason(HttpServletRequest req, @RequestBody Map<String, Object> params) {
        try {
            params.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
            new JdbcUtils(SQL_FILENAME).update("insertExportReason", params);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 실시간 현황 멀티 필터링 - 필터 저장 폼
     *
     * @param req
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/multi/filter/form", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String multiFilterForm(HttpServletRequest req, @PathVariable("svc") String svc, @RequestParam("page") String page, @RequestParam("f") String func) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", page);
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));

        result.put("page", page);
        result.put("f", func); // callback function

        return WebUtil.parseWebPageResourceSafe("/html/v2/common/filter_form.html", result, svc);
    }

    /**
     * 실시간 현황 멀티 필터링 - 필터 기본값 변경
     *
     * @param req
     * @param params
     * @return
     */
    @RequestMapping(value = "/multi/filter/default", method = RequestMethod.POST)
    @ResponseBody
    public Boolean updateDefaultMultiFilter(HttpServletRequest req, @RequestBody Map<String, Object> params) {
        try {
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));

            if ("Y".equals(params.get("defaultYn"))) {
                /* 기본값 on */
                paramMap.put("page", params.get("page"));
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap); // 기존 필터 기본값 off

                paramMap.put("colNm", params.get("colNm"));
                paramMap.put("defaultYn", "Y");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap); // 변경 대상 필터 기본값 on

            } else {
                /* 기본값 off */
                paramMap.put("page", params.get("page"));
                paramMap.put("colNm", params.get("colNm"));
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap); // 변경 대상 필터 기본값 off
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 실시간 현황 멀티 필터링 - 필터관리 저장 (이름변경 및 삭제)
     *
     * @param req
     * @param params
     * @return
     */
    @RequestMapping(value = "/multi/filter/manage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> multiFilterManage(HttpServletRequest req, @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> updated = (List<Map<String, Object>>) params.get("updated");
            List<Map<String, Object>> deleted = (List<Map<String, Object>>) params.get("deleted");

            if (updated.isEmpty() && deleted.isEmpty()) {
                result.put("result", false);
                result.put("message", "변경 사항이 없습니다");

                return result;
            }

            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            // 이름변경
            for (Map<String, Object> paramMap : updated) {
                if (paramMap.get("userId") == null) {
                    paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
                }
                jdbcUtils.update("updateMultiFilterNm", paramMap);
            }

            // 삭제
            for (Map<String, Object> paramMap : deleted) {
                if (paramMap.get("userId") == null) {
                    paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
                }
                jdbcUtils.update("deleteMultiFilterData", paramMap);
                jdbcUtils.update("deleteMultiFilter", paramMap);
            }

            result.put("result", true);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }

        return result;
    }

    /**
     * 실시간 현황 멀티 필터링 - 필터관리
     *
     * @param req
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/multi/filter", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String filter(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/v2/common/filter.html", result, svc);
    }

    /**
     * 실시간 현황 멀티 필터링 - 신규 저장
     *
     * @param req
     * @param params
     * @return
     */
    @RequestMapping(value = "/multi/filter", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateMultiFilter(HttpServletRequest req, @RequestBody MultiFilterRecord params) {
        Map<String, Object> result = new HashMap<>();

        try {
            String userId = DecisionServiceUtils.getUserNameFromRequestCookie(req);

            if (StringUtils.isEmpty(userId)) {
                result.put("result", false);
                result.put("message", "로그인 정보가 만료되었습니다");

                return result;
            }

            Gson gson = new Gson();
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);
            paramMap.put("page", params.getPage());
            paramMap.put("type", params.getType());
            paramMap.put("colNm", params.getColNm());

            // 덮어쓰기 아닌 경우, 필터명 중복 체크
            if (!"Y".equals(params.getMergeYn()) && paramMap.get("colNm") != null) {
                List<Map<String, Object>> dup = jdbcUtils.queryForList("selectMultiFilter", paramMap);

                if (dup != null && !dup.isEmpty()) {
                    result.put("result", false);
                    result.put("message", "중복된 필터명입니다");

                    return result;
                }
            }

            // 기존 필터 삭제 (필터명 기준)
            jdbcUtils.update("deleteMultiFilterData", paramMap);
            jdbcUtils.update("deleteMultiFilter", paramMap);

            paramMap.remove("colNm");

            // 기본값으로 등록하는 경우, 기존 필터 기본값 off
            if ("Y".equals(params.getDefaultYn())) {
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap);
            }

            List<MultiFilterRecord> dataList = params.getCols();

            for (MultiFilterRecord record : dataList) {
                Integer seq = jdbcUtils.queryForObject("selectMultiFilterSeq", Integer.class);
                record.setSeq(seq);

                if (StringUtils.isEmpty(record.getUserId())) {
                    record.setUserId(userId);
                }

                // 상담사현황 직책 선택값은, 해당하는 팝업에 대한 seq값을 가지고 있어야 함
                if ("CHART_POSITION".equals(record.getType())) {
                    Optional<MultiFilterRecord> chart = dataList.stream().parallel().filter(s -> ("CHART_GROUP".equals(s.getType()) || "CHART_TEAM".equals(s.getType())) && StringUtils.equals(record.getCol(), s.getCol())).findAny();
                    if (chart != null && chart.isPresent() && chart.get().getSeq() != null) {
                        record.setCol(chart.get().getSeq().toString());
                    }
                }

                jdbcUtils.update("insertMultiFilter", record); // 필터 항목 저장

                List<MultiFilterDataRecord> list = record.getValues();
                for (MultiFilterDataRecord r : list) {
                    r.setColSeq(seq);

                    if (StringUtils.isEmpty(r.getUserId())) {
                        r.setUserId(userId);
                    }
                }

                jdbcUtils.batchUpdate("insertMultiFilterData", list); // 필터 값 저장
            }

            result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
            result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));
            result.put("result", true);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }

        return result;
    }

    /**
     * 실시간 현황 멀티 필터링 - 필터 삭제
     *
     * @param req
     * @param params
     * @return
     */
    @RequestMapping(value = "/multi/filter", method = RequestMethod.DELETE)
    @ResponseBody
    public String multiFilterDelete(HttpServletRequest req, @RequestBody List<Map<String, Object>> params) {
        try {
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            for (Map<String, Object> p : params) {
                p.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));

                jdbcUtils.update("deleteMultiFilterData", p);
                jdbcUtils.update("deleteMultiFilter", p);
            }

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
        setChannel(result); // 모바일,홈,기업

        // 권한에 따른 별도 팝업 개수 제한 설정이 없는 경우, default 적용
        if (!result.containsKey("popupMaxCount") || result.get("popupMaxCount") == null) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("grp_nm", "popup_max_count");
            paramMap.put("comm_nm", "popup_max_count");
            paramMap.put("comm_value", "default");
            paramMap.put("use_yn", "Y");
            Map<String, Object> map = new JdbcUtils(SQL_FILENAME).queryForMap("selectComm", paramMap);

            if (map != null) {
                Map<String, Object> popupMaxCount = new HashMap<>();
                popupMaxCount.put("CHART_GROUP", map.get("param2"));
                popupMaxCount.put("CHART_TEAM", map.get("param3"));
                popupMaxCount.put("ROUTE", map.get("param4"));
                popupMaxCount.put("QMONITOR", map.get("param5"));
                result.put("popupMaxCount", popupMaxCount);
            }
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
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        // 역할 체크
        List<String> roles = result.get("customRoleNames") != null ? (List<String>) result.get("customRoleNames") : null;

        paramMap.put("grp_nm", "search");
        paramMap.put("comm_nm", "searchmaster");
        paramMap.put("use_yn", "Y");
        List<Map<String, Object>> masterList = jdbcUtils.queryForList("selectComm", paramMap);

        Boolean isMaster = false;
        Optional<Map<String, Object>> masterRole = masterList.stream().filter(e -> roles.contains(e.get("comm_value"))).findAny();
        if (masterRole != null && masterRole.isPresent()) {
            isMaster = true;

            // 팝업 개수 제한
            if ("superusers".equals(masterRole.get().get("comm_value"))) {
                result.put("popupMaxCount", new HashMap<>());
            }

            if ("Y".equals(masterRole.get().get("param1"))) {
                Map<String, Object> popupMaxCount = new HashMap<>();
                popupMaxCount.put("CHART_GROUP", masterRole.get().get("param2"));
                popupMaxCount.put("CHART_TEAM", masterRole.get().get("param3"));
                popupMaxCount.put("ROUTE", masterRole.get().get("param4"));
                popupMaxCount.put("QMONITOR", masterRole.get().get("param5"));

                result.put("popupMaxCount", popupMaxCount);
            }
        }

        // 모든 채널
        paramMap.put("depth", 1);
        List<Map<String, Object>> channelInfo = jdbcUtils.queryForList("selectOrgInfo", paramMap);

        // 소속 채널
        String myChannel = "";
        if (result.get("departmentId") != null && !"".equals(result.get("departmentId"))) {
            paramMap = new HashMap<>();
            paramMap.put("orgCd", result.get("departmentId"));
            Map<String, Object> myOrg = jdbcUtils.queryForMap("selectOrgInfo", paramMap);

            if (myOrg != null) {
                myChannel = String.valueOf(myOrg.get("chn_nm"));
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

        result.put("isMaster", isMaster);
        result.put("myChannel", myChannel);
        result.put("channels", channels);
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