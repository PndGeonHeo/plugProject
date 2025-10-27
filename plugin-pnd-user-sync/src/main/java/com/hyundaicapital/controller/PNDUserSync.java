package com.hyundaicapital.controller;


import com.fr.base.TemplateUtils;
import com.fr.data.NetworkHelper;
import com.fr.decision.authority.data.User;
import com.fr.decision.config.FSConfig;
import com.fr.decision.mobile.terminal.TerminalHandler;
import com.fr.decision.record.OperateMessage;
import com.fr.decision.webservice.Response;
import com.fr.decision.webservice.annotation.FinePathVariable;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.utils.ControllerFactory;
import com.fr.decision.webservice.utils.controller.AuthenticController;
import com.fr.decision.webservice.v10.login.LoginService;
import com.fr.decision.webservice.v10.user.SyncService;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.intelli.record.MetricRegistry;
import com.fr.log.FineLoggerFactory;
import com.fr.schedule.feature.service.v10.ScheduleTaskService;
import com.fr.stable.web.Device;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.RequestMapping;
import com.fr.third.springframework.web.bind.annotation.RequestMethod;
import com.fr.third.springframework.web.bind.annotation.RequestParam;
import com.fr.third.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Controller
@LoginStatusChecker(required = false)
public class PNDUserSync {

    @RequestMapping(value = {"/onemore/once"}, method = {RequestMethod.GET})
    @ResponseBody
    @LoginStatusChecker(required = false)
    public Response once() throws Exception {
        try {
            FineLoggerFactory.getLogger().error("================ HYUNDAICAPITAL USER SYNC ================");
            SyncService.getInstance().synchronize();
            FineLoggerFactory.getLogger().error("================ HYUNDAICAPITAL USER SYNC END ================");
        }catch (Exception e ){
            FineLoggerFactory.getLogger().error("HYUNDAICAPITAL USER SYNC ERROR");
            FineLoggerFactory.getLogger().error(e.toString());
        }
        return Response.success();
    }
}