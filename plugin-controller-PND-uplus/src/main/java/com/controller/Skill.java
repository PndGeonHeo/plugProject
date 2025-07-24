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

@Controller("v1Skill")
@RequestMapping(value="/{svc}/skill")
@LoginStatusChecker(required = false)
public class Skill {
    private static String SQL_FILENAME = "sqlmap.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 큐/스킬 현황 - 컨테이너 (큐, 스킬 각 영역 html 별도 분리)
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/container", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String container(HttpServletRequest req, @RequestParam(value = "type", required = false) String type, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "QUEUE");
        result.put("qFilter", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        paramMap.put("page", "SKILL");
        result.put("skillFilter", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        result.put("type", type);

        return WebUtil.parseWebPageResourceSafe("/html/skill/container.html", result, svc);
    }

    /**
     * 큐 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queue", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String queue(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 채널
        List<Map<String, Object>> channelInfo = new Common(serviceContext).getChannelByAuth(req);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 큐정보
        if (channelInfo.size() > 0) {
            paramMap.put("channels", new ArrayList(channelInfo.stream().map(e -> e.get("org_name")).collect(Collectors.toList()))); // 조회 가능 채널
            result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));
        }

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "QUEUE");
        result.put("qFilter", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        // 팝업 개수 제한
        paramMap = new HashMap<>();
        paramMap.put("grp_nm", "popup_max_count");
        paramMap.put("use_yn", "Y");
        result.put("popupMaxCount", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        return WebUtil.parseWebPageResource("/html/skill/queue.html", result, svc);
    }

    /**
     * 큐/스킬현황 > 라우트 현황
     *
     * @param queue
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/route", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String route(@RequestParam("queue") List<String> queue, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        paramMap.put("ids", queue);
        result.put("qInfo", new Gson().toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/skill/route_popup.html", result, svc);
    }

    /**
     * 큐/스킬현황 > 큐모니터
     *
     * @param queue
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qmonitor", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String qmonitor(@RequestParam("queue") List<String> queue, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

        paramMap.put("ids", queue);
        result.put("qInfo", new Gson().toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/skill/qmonitor_popup.html", result, svc);
    }

    /**
     * 스킬 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String skill(HttpServletRequest req, @RequestParam(value = "type", required = false) String type, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        // 채널
        List<Map<String, Object>> channelInfo = new Common(serviceContext).getChannelByAuth(req);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 스킬정보
        if (channelInfo.size() > 0) {
            paramMap.put("channels", new ArrayList(channelInfo.stream().map(e -> e.get("org_name")).collect(Collectors.toList()))); // 조회 가능 채널
            result.put("skillInfo", gson.toJson(jdbcUtils.queryForList("selectSkill", paramMap)));
        }

        // type=org인 경우, 조직(그룹)으로 한 번 더 그룹핑 (홈/기업 사용)
        if ("org".equals(type)) {
            paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
            paramMap.put("page", "SKILL_ORG");
            result.put("skillFilter", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

            return WebUtil.parseWebPageResource("/html/skill/skill_by_org.html", result, svc);
        }

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "SKILL");
        result.put("skillFilter", gson.toJson(jdbcUtils.queryForList("selectFilter", paramMap)));

        return WebUtil.parseWebPageResource("/html/skill/skill.html", result, svc);
    }

    /**
     * 스킬 현황 조회
     * > 그룹핑 기준 : 스킬(또는 스킬그룹) -> 모바일만 사용
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajax", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> skillAjax(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        paramMap.put("listCols", params.get("listCols"));

        boolean groupFlag = (boolean) params.get("groupFlag");
        if (groupFlag) {
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillGroupStatus", paramMap)));

        } else {
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillStatus", paramMap)));
        }

        return result;
    }

    /**
     * 스킬 현황 조회
     * > 그룹핑 기준 : 스킬(또는 스킬그룹) + 조직(그룹) -> 홈/기업 사용
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/org/ajax", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> skillAjaxByOrg(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        paramMap.put("listCols", params.get("listCols"));

        boolean groupFlag = (boolean) params.get("groupFlag");
        if (groupFlag) {
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillGroupStatusByOrg", paramMap)));

        } else {
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillStatusByOrg", paramMap)));
        }

        return result;
    }
}