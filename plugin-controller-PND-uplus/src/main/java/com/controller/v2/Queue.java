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
@RequestMapping(value="/{svc}/v2/queue")
@LoginStatusChecker(required = false)
public class Queue {
    private static String SQL_FILENAME = "sqlmap_v2.xml";

    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * 큐 현황
     *
     * @param req
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String queue(HttpServletRequest req, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        Map<String, Object> depAndRoles = new Common(serviceContext).depAndRoles(req, null);

        List<Map<String, Object>> channelInfo = (List<Map<String, Object>>) depAndRoles.get("channels");
        result.put("channelInfo", gson.toJson(channelInfo));
        result.put("popupMaxCount", gson.toJson(depAndRoles.get("popupMaxCount")));

        // 큐정보
        if (channelInfo.size() > 0) {
            paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
            result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        } else {
            result.put("qInfo", gson.toJson(new ArrayList<>()));
        }

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "QUEUE");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/v2/queue/queue.html", result, svc);
    }

    /**
     * 큐 현황 > 라우트 현황
     *
     * @param queue
     * @param alert
     * @param collapse
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/route", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String route(@RequestParam("queue") List<String> queue, @RequestParam(value = "alert", required = false) Boolean alert, @RequestParam(value = "collapse", required = false) Boolean collapse, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        paramMap.put("ids", queue);
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        result.put("queue", gson.toJson(queue));
        result.put("alert", alert);
        result.put("collapse", collapse != null ? collapse : false);

        return WebUtil.parseWebPageResourceSafe("/html/v2/queue/route_popup.html", result, svc);
    }

    /**
     * 큐 현황 > 큐모니터
     *
     * @param queue
     * @param alert
     * @param svc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qmonitor", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String qmonitor(@RequestParam("queue") List<String> queue, @RequestParam(value = "alert", required = false) Boolean alert, @PathVariable("svc") String svc) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);
        Gson gson = new Gson();

        paramMap.put("ids", queue);
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        // 새로고침 시간 (초 단위)
        paramMap.put("grp_nm", "qmonitor_reload");
        paramMap.put("use_yn", "Y");
        Map<String, Object> map = jdbcUtils.queryForMap("selectComm", paramMap);

        if (map != null && !map.isEmpty()) {
            result.put("reloadTime", map.get("comm_value"));
        }

        result.put("queue", gson.toJson(queue));
        result.put("alert", alert);

        return WebUtil.parseWebPageResourceSafe("/html/v2/queue/qmonitor_popup.html", result, svc);
    }
}