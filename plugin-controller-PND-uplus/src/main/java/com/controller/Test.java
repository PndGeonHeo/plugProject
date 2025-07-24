package com.controller;

import com.common.JdbcUtils;
import com.common.WebUtil;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.utils.DecisionServiceUtils;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.org.apache.commons.lang3.StringUtils;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.http.MediaType;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.record.FilterRecord;
import com.record.MultiFilterDataRecord;
import com.record.MultiFilterRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value="/{svc}/demo")
@LoginStatusChecker(required = false)
public class Test {
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
    @RequestMapping(value = "/agent/table", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_TABLE");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        // 팝업 개수 제한
        paramMap = new HashMap<>();
        paramMap.put("grp_nm", "popup_max_count");
        paramMap.put("use_yn", "Y");
        result.put("popupMaxCount", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_table.html", result, svc);
    }

    /**
     * 상담사 개별 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/agent/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));

        // 상담원정보
        result.put("userInfo", gson.toJson(jdbcUtils.queryForList("selectAgent", paramMap)));

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "AGENT_LIST");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_list.html", result, svc);
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

        // 팝업 개수 제한
        paramMap = new HashMap<>();
        paramMap.put("grp_nm", "popup_max_count");
        paramMap.put("use_yn", "Y");
        result.put("popupMaxCount", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/skill/queue.html", result, svc);
    }

    /**
     * 필터 저장 폼
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

        return WebUtil.parseWebPageResourceSafe("/html/common/filter_form.html", result, svc);
    }

    /**
     * 필터 기본값 변경
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
                paramMap.put("page", params.get("page"));
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap);

                paramMap.put("colNm", params.get("colNm"));
                paramMap.put("defaultYn", "Y");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap);

            } else {
                paramMap.put("page", params.get("page"));
                paramMap.put("colNm", params.get("colNm"));
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 필터 삭제
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
     * 실시간 현황 필터관리 저장
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
     * 임계치 설정
     *
     * @param req
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

        return WebUtil.parseWebPageResourceSafe("/html/status/threshold.html", result, svc);
    }

    /**
     * 실시간 상담사 현황 > 상담사현황 팝업
     *
     * @param orgCds
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/agent/chart", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
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
            return WebUtil.parseWebPageResourceSafe("/html/status/agent_team_chart_popup.html", result, svc);
        }

        return WebUtil.parseWebPageResourceSafe("/html/status/agent_chart_popup.html", result, svc);
    }

    /**
     * 스킬 현황
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/skill", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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

            return WebUtil.parseWebPageResourceSafe("/html/skill/skill_by_org.html", result, svc);
        }

        // 필터
        paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
        paramMap.put("page", "SKILL");
        result.put("filterData", gson.toJson(jdbcUtils.queryForList("selectMultiFilter", paramMap)));
        result.put("filterValue", gson.toJson(jdbcUtils.queryForList("selectMultiFilterData", paramMap)));

        return WebUtil.parseWebPageResourceSafe("/html/skill/skill.html", result, svc);
    }

    /**
     * 팝업 전체 조회 (상담사현황, 큐모니터, 라우트현황)
     *
     * @param req
     * @param svc
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
        result.put("myChannel", myChannel);
        result.put("channelInfo", gson.toJson(channelInfo));

        // 역할에 따른 조회 권한 (별도로 부여된 권한이 없는 경우, 소속 팀 조회만 가능)
        result.put("viewLevels", gson.toJson(jdbcUtils.queryForList("selectViewLevel", null)));

        // 조직 및 직책 정보
        paramMap.put("channels", Arrays.asList(channelInfo.stream().map(e -> e.get("org_name")).toArray())); // 조회 가능 채널
        result.put("orgInfo", gson.toJson(jdbcUtils.queryForList("selectOrgInfo", paramMap)));
        result.put("position", gson.toJson(jdbcUtils.queryForList("selectPosition", paramMap)));

        // 큐정보
        result.put("qInfo", gson.toJson(jdbcUtils.queryForList("selectQueue", paramMap)));

        // 팝업 개수 제한
        paramMap.put("grp_nm", "popup_max_count");
        paramMap.put("use_yn", "Y");
        result.put("popupMaxCount", gson.toJson(jdbcUtils.queryForList("selectComm", paramMap)));

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

        return WebUtil.parseWebPageResourceSafe("/html/common/popup_monitor.html", result, svc);
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

        return WebUtil.parseWebPageResourceSafe("/html/common/filter.html", result, svc);
    }

    /**
     * 실시간 현황 멀티 필터링 - 저장
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
            Gson gson = new Gson();
            JdbcUtils jdbcUtils = new JdbcUtils(SQL_FILENAME);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", DecisionServiceUtils.getUserNameFromRequestCookie(req));
            paramMap.put("page", params.getPage());
            paramMap.put("type", params.getType());
            paramMap.put("colNm", params.getColNm());

            if (!"Y".equals(params.getMergeYn()) && paramMap.get("colNm") != null) {
                List<Map<String, Object>> dup = jdbcUtils.queryForList("selectMultiFilter", paramMap);

                if (dup != null && !dup.isEmpty()) {
                    result.put("result", false);
                    result.put("message", "중복된 필터명입니다");

                    return result;
                }
            }

            jdbcUtils.update("deleteMultiFilterData", paramMap);
            jdbcUtils.update("deleteMultiFilter", paramMap);

            paramMap.remove("colNm");

            if ("Y".equals(params.getDefaultYn())) {
                paramMap.put("defaultYn", "N");
                jdbcUtils.update("updateDefaultMultiFilter", paramMap);
            }

            List<MultiFilterRecord> dataList = params.getCols();

            for (MultiFilterRecord record : dataList) {
                Integer seq = jdbcUtils.queryForObject("selectMultiFilterSeq", Integer.class);
                record.setSeq(seq);

                if ("CHART_POSITION".equals(record.getType())) {
                    Optional<MultiFilterRecord> chart = dataList.stream().parallel().filter(s -> ("CHART_GROUP".equals(s.getType()) || "CHART_TEAM".equals(s.getType())) && StringUtils.equals(record.getCol(), s.getCol())).findAny();
                    if (chart != null && chart.isPresent() && chart.get().getSeq() != null) {
                        record.setCol(chart.get().getSeq().toString());
                    }
                }

                jdbcUtils.update("insertMultiFilter", record);

                List<MultiFilterDataRecord> list = record.getValues();
                for (MultiFilterDataRecord r : list) {
                    r.setColSeq(seq);
                }

                jdbcUtils.batchUpdate("insertMultiFilterData", list);
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
}