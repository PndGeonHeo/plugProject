package com.controller;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("v1Agent")
@RequestMapping(value="/{svc}/agent")
@LoginStatusChecker(required = false)
public class Agent {
    private static String SQL_FILENAME = "sqlmap.xml";

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
        result.put("myChannel", myChannel);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 역할에 따른 조회 권한 (별도로 부여된 권한이 없는 경우, 소속 팀 조회만 가능)
        result.put("viewLevels", gson.toJson(jdbcUtils.queryForList("selectViewLevel", null)));

        // 조직정보
        paramMap.put("channels", new ArrayList(channelInfo.stream().map(e -> e.get("org_name")).collect(Collectors.toList()))); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_TABLE");
        result.put("filterInfo", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        // 팝업 개수 제한
        paramMap = new HashMap<>();
        paramMap.put("grp_nm", "popup_max_count");
        paramMap.put("use_yn", "Y");
        result.put("popupMaxCount", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_table.html", result, svc);
    }

    /**
     * 실시간 상담사 현황 > 차트(그룹별 현황)
     *
     * @param groups
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chart", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String chart(@RequestParam("group") List<String> groups, @RequestParam(value = "alert", required = false) String alert, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 그룹정보
        paramMap.put("orgCds", groups);
        result.put("grpInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        paramMap.put("groups", groups);
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        result.put("alert", alert);

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_chart_popup.html", result, svc);
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
        result.put("viewLevels", gson.toJson(jdbcUtils.queryForList("selectViewLevel", null)));

        // 조직정보
        paramMap.put("channels", new ArrayList(channelInfo.stream().map(e -> e.get("org_name")).collect(Collectors.toList()))); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_LIST");
        result.put("filterInfo", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_list.html", result, svc);
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
    public String threshold(HttpServletRequest req, @RequestParam("page") String page, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        // 임계치 설정 조회
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", page);
        paramMap.put("type", "THRESHOLD");
        result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/threshold_popup.html", result, svc);
    }

    /**
     * 실시간 상담사 현황 - 매장
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pos", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String pos(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 담당
        Map<String, Object> depAndRoles = new Common(serviceContext).depAndRoles(req, "AGENT_POS");
        String myDamdang = "";
        if (depAndRoles.get("myDamdang") != null) {
            myDamdang = (String) depAndRoles.get("myDamdang");
        }
        List<Map<String, Object>> damdangInfo = new ArrayList<>();
        if (depAndRoles.get("damdangs") != null) {
            damdangInfo = (List<Map<String, Object>>) depAndRoles.get("damdangs");
        }

        result.put("departmentId", depAndRoles.get("departmentId"));
        result.put("myDamdang", myDamdang);
        result.put("damdangInfo", gson.toJson(damdangInfo));

        // 조직정보
        paramMap.put("damdangs", new ArrayList(damdangInfo.stream().map(e -> e.get("org_cd")).collect(Collectors.toList()))); // 조회 가능 담당
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfoPos", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_POS");
        result.put("filterInfo", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_pos.html", result, svc);
    }

    /**
     * 실시간 상담사 현황 조회 - 매장
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pos/ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> posAjax(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 상담원 로그인 수
        List<String> orgCds = (List<String>) params.get("orgCds");
        paramMap.put("orgCds", orgCds);
        result.put("loginCnt", gson.toJson(jdbcUtils.queryForList("selectAgentPosLoginCnt", paramMap)));

        // 상담원
        paramMap.put("searchType", params.get("searchType"));
        result.put("data", gson.toJson(jdbcUtils.queryForList("selectAgentPos", paramMap)));
        
        result.put("orgCds", gson.toJson(orgCds));

        return result;
    }

    /**
     * 실시간 상담사 현황 - 매장 > 차트(그룹별 현황)
     *
     * @param orgCds
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pos/chart", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String posChart(@RequestParam("orgCds") List<String> orgCds, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        // 상담원 로그인 수
        paramMap.put("orgCds", orgCds);
        result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectAgentPosLoginCnt", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_pos_chart_popup.html", result, svc);
    }
}