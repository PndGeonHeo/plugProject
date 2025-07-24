package com.controller.v2;

import com.common.JdbcUtils;
import com.common.WebUtil;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.utils.DecisionServiceUtils;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.http.MediaType;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value="/{svc}/v2/agent")
@LoginStatusChecker(required = false)
public class Agent {
    private static String SQL_FILENAME = "sqlmap_v2.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 실시간 상담사 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/table", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String table(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
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

        // 조직정보
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_TABLE");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/v2/status/agent_table.html", result, svc);
    }

    /**
     * 실시간 상담사 현황 > 차트(그룹별 현황)
     *
     * @param orgCds
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chart", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String chart(@RequestParam("org") List<String> orgCds, @RequestParam(value = "position", required = false) List<String> position, @RequestParam("type") String type, @RequestParam(value = "alert", required = false) String alert, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 조직정보
        paramMap.put("orgCds", orgCds);
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 직책정보
        result.put("position", gson.toJson(position));

        result.put("orgCds", gson.toJson(orgCds));
        result.put("alert", alert);

        if ("CHART_TEAM".equals(type)) {
            return WebUtil.parseWebPageResourceSafe("/html/v2/status/agent_team_chart_popup.html", result, svc);
        }

        return WebUtil.parseWebPageResourceSafe("/html/v2/status/agent_chart_popup.html", result, svc);
    }

    /**
     * 상담사 개별 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String list(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
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
        result.put("myChannel", myChannel);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 역할에 따른 조회 권한 (별도로 부여된 권한이 없는 경우, 소속 팀 조회만 가능)
        paramMap.put("grp_nm", "realtime");
        paramMap.put("use_yn", "Y");
        result.put("viewLevels", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        // 조직정보
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_LIST");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/v2/status/agent_list.html", result, svc);
    }

    /**
     * 임계치 설정 팝업
     *
     * @param req
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/threshold", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String threshold(HttpServletRequest req, @PathVariable("svc") String svc, @RequestParam("page") String page) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 조직정보
        List<Map<String, Object>> channelInfo = new Common(serviceContext).getChannelByAuth(req);
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 임계치 설정 조회
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", page);
        paramMap.put("type", "THRESHOLD");
        result.put("data", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("dataValues", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        result.put("page", page);

        return WebUtil.parseWebPageResourceSafe("/html/v2/status/threshold.html", result, svc);
    }
}