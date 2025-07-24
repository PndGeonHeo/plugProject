package com.controller;

import com.common.JdbcUtils;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.utils.DecisionServiceUtils;
import com.fr.decision.webservice.utils.WebServiceUtils;
import com.fr.third.springframework.beans.factory.annotation.Qualifier;
import com.fr.third.springframework.http.MediaType;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/uplus/skill")
@LoginStatusChecker(required = false)
public class Skill {
    private ServiceContext serviceContext;

    public Skill(@Qualifier("decision") ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 큐/스킬 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String skill(HttpServletRequest req) throws Exception {
        Map<String, Object> result = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils();
        Gson gson = new Gson();

        // 큐정보
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("channels", new Common(serviceContext).getChannelByAuth(req)); // 조회 가능 채널

        String sql = "with q_data as ( "
                   + "    select gq.id, gq.name, gq.description, gq.etc1, gq.etc2, "
                   + "           voi.org_cd as center, voi.org_name as center_name, voi.hirk_org_cd as channel "
                   + "    from   gc_queue gq "
                   + "           left join wdm.v_org_info voi on voi.org_name = gq.etc1 and voi.depth = 2 "
                   + "    where  coalesce(etc2, '') != '' "
                   + "    and    voi.hirk_org_cd in (:channels) "
                   + ") "
                   + "select t.id, t.name, t.group, t.depth, t.center_name, t.center, t.center_name, t.channel "
                   + "from   ( "
                   + "           select etc2 as id, etc2 as name, '' as group, 1 as depth, center, center_name, channel from q_data group by etc1, etc2, center, center_name, channel "
                   + "           union all "
                   + "           select id, concat(name , '_' , description) as name, etc2 as group, 2 as depth, center, center_name, channel from q_data "
                   + "       ) t "
                   + "order by t.depth, t.name ";
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        // 스킬정보
        sql = "with skill_data as ( "
            + "    select gs.id, gs.name, gs.etc1, gs.etc2, "
            + "           voi.org_cd as center, voi.org_name as center_name, voi.hirk_org_cd as channel "
            + "    from   gc_skill gs "
            + "           left join finer.comm c on c.comm_value = substring(gs.name, 1, position('_' in gs.name) - 1) and c.grp_nm = '센터' "
            + "           left join wdm.v_org_info voi on voi.org_cd = c.param2 and voi.depth = 2 "
            + "    where  coalesce(etc2, '') != '' "
            + "    and    voi.hirk_org_cd in (:channels) "
            + ") "
            + "select t.id, t.name, t.group, t.depth, t.center_name, t.center, t.center_name, t.channel "
            + "from   ( "
            + "           select etc2 as id, etc2 as name, '' as group, 1 as depth, center, center_name, channel from skill_data group by etc2, center, center_name, channel "
            + "           union all "
            + "           select id, substring(name, position('_' in name) + 1) as name, etc2 as group, 2 as depth, center, center_name, channel from skill_data "
            + "       ) t "
            + "order by t.depth, t.name";
        result.put("skillInfo", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        // 필터
        paramMap.put("userName", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("pages", Arrays.asList("QUEUE", "SKILL"));

        sql = "select rcp.user_id, rcp.page, rcp.type, rcp.col, rcp.sort, rcp.value "
            + "from   finer.realtime_col_personalize rcp "
            + "where  rcp.user_id = :userName "
            + "and    rcp.page in (:pages) "
            + "order by rcp.page, rcp.type, rcp.sort";
        result.put("filterInfo", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        return WebServiceUtils.parseWebPageResourceSafe("/html/skill/skill.html", result);
    }

    /**
     * 큐/스킬현황 > 라우트 현황
     *
     * @param queue
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/route", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String route(@RequestParam("queue") List<String> queue) throws Exception {
        Map<String, Object> result = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils();
        Gson gson = new Gson();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("queue", queue);
        String sql = "select id, concat(name , '_' , description) as name, etc2 as group, 2 as depth "
                   + "from   gc_queue "
                   + "where  id in (:queue) "
                   + "order by etc2, name";
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        return WebServiceUtils.parseWebPageResourceSafe("/html/skill/route_popup.html", result);
    }

    /**
     * 큐/스킬현황 > 큐모니터
     *
     * @param qgroups
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qmonitor", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String qmonitor(@RequestParam("qgroup") List<String> qgroups) throws Exception {
        Map<String, Object> result = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils();
        Gson gson = new Gson();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("qgroups", qgroups);
        String sql = "select id, concat(name , '_' , description) as name, etc2 as group, 2 as depth "
                   + "from   gc_queue "
                   + "where  etc2 in (:qgroups) "
                   + "order by etc2, name";
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        return WebServiceUtils.parseWebPageResourceSafe("/html/skill/qmonitor_popup.html", result);
    }

    /**
     * 스킬 현황 조회
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> skillAjax(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        Boolean groupFlag = (Boolean) params.get("groupFlag");
        List<String> listCols = (List<String>) params.get("listCols");

        JdbcUtils jdbcUtils = new JdbcUtils();
        Gson gson = new Gson();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("listCols", listCols);

        String sql = "";
        
        if (groupFlag) {
            sql = "select gs.etc2 as name "
                + "      ,count(t.agid) as login "
                + "      ,sum(case when t.status = 3 then 1 else 0 end) as wait "
                + "      ,sum(case when t.status = 4 then 1 else 0 end) as aux "
                + "      ,sum(case when t.status = 5 then 1 else 0 end) as acw "
                + "      ,sum(case when t.status = 14 then 1 else 0 end) as ob "
                + "      ,sum(case when t.status = 15 then 1 else 0 end) as ib "
                + "      ,sum(case when t.status not in (3, 4, 5, 14, 15) then 1 else 0 end) as etc "
                + "from   gc_skill gs "
                + "       left join ( "
                + "           select c.agid, c.skill, case when c.status > 5 then c.status else c.login_status end as status "
                + "           from cagent c "
                + "           where c.login_status not in (0, 1) "
                + "       ) t on gs.id = t.skill "
                + "where  gs.etc2 in (:listCols) "
                + "group by gs.etc2";

        } else {
            sql = "select gs.id "
                + "      ,substring(gs.name, position('_' in gs.name) + 1) as name "
                + "      ,count(t.agid) as login "
                + "      ,sum(case when t.status = 3 then 1 else 0 end) as wait "
                + "      ,sum(case when t.status = 4 then 1 else 0 end) as aux "
                + "      ,sum(case when t.status = 5 then 1 else 0 end) as acw "
                + "      ,sum(case when t.status = 14 then 1 else 0 end) as ob "
                + "      ,sum(case when t.status = 15 then 1 else 0 end) as ib "
                + "      ,sum(case when t.status not in (3, 4, 5, 14, 15) then 1 else 0 end) as etc "
                + "from   gc_skill gs "
                + "       left join ( "
                + "           select c.agid, c.skill, case when c.status > 5 then c.status else c.login_status end as status "
                + "           from cagent c "
                + "           where c.login_status not in (0, 1) "
                + "       ) t on gs.id = t.skill "
                + "where  gs.id in (:listCols) "
                + "group by gs.id";
        }
        
        result.put("data", gson.toJson(jdbcUtils.queryForList(sql, paramMap)));

        return result;
    }
}