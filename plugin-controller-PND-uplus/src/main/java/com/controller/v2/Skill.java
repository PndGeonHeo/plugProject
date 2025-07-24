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
@RequestMapping(value="/{svc}/v2/skill")
@LoginStatusChecker(required = false)
public class Skill {
    private static String SQL_FILENAME = "sqlmap_v2.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 스킬 현황
     *
     * @param req
     * @param type
     * @param svc
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
            paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
            result.put("skillInfo", gson.toJson(jdbcUtils.queryForList("selectSkill", paramMap)));

        } else {
            result.put("skillInfo", gson.toJson(new ArrayList<>()));
        }

        // type=org인 경우, 조직(그룹)으로 한 번 더 그룹핑 (홈/기업 사용)
        if ("org".equals(type)) {
            paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
            paramMap.put("page", "SKILL_ORG");
            result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
            result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

            return WebUtil.parseWebPageResourceSafe("/html/v2/skill/skill_by_org.html", result, svc);
        }

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "SKILL");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/v2/skill/skill.html", result, svc);
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

        Boolean groupFlag = (Boolean) params.get("groupFlag");
        if (groupFlag) {
            paramMap.put("listGroupCols", params.get("listGroupCols"));
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillGroupStatus", paramMap)));

        } else {
            paramMap.put("listCols", params.get("listCols"));
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

        Boolean groupFlag = (Boolean) params.get("groupFlag");
        if (groupFlag) {
            paramMap.put("listGroupCols", params.get("listGroupCols"));
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillGroupStatusByOrg", paramMap)));

        } else {
            paramMap.put("listCols", params.get("listCols"));
            result.put("data", new Gson().toJson(jdbcUtils.queryForList("selectSkillStatusByOrg", paramMap)));
        }

        return result;
    }
}