package com.controller.develop;

import com.common.develop.Common;
import com.common.develop.CommonPropertiesUtil;
import com.common.develop.LoginConfig;
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
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Controller
@LoginStatusChecker(required = false)
public class Param {




    @RequestMapping(value = "/dasTest", method = RequestMethod.GET)
    @ResponseBody
    public String DasTest() throws IOException {
        String decryptType = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","das.test", CommLogin.class);
        return decryptType;
    }
    @RequestMapping(value = {"/task/{taskName}/exists"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response checkTaskExits(HttpServletRequest paramHttpServletRequest, @FinePathVariable("taskName") String paramString2) throws Exception {
        return Response.ok(Boolean.valueOf(ScheduleTaskService.getInstance().checkTaskExists(paramString2)));
    }

    @RequestMapping(value = {"/task/{taskName}/exists/string"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response checkTaskExitss(HttpServletRequest paramHttpServletRequest, @FinePathVariable("taskName") String paramString2) throws Exception {
        return Response.ok(String.valueOf(ScheduleTaskService.getInstance().checkTaskExists(paramString2)));
    }

    /* Task Name을 통해, ID 찾기 */
    @RequestMapping(value = {"/task/{taskName}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response taskByNamed(HttpServletRequest paramHttpServletRequest, @FinePathVariable("taskName") String paramString2) throws Exception {

        //JSONObject outuer = new JSONObject(String.valueOf(ScheduleTaskService.getInstance().findTaskByName(paramString2)));
        //String innerJsonString = outuer.getString("data");
        //JSONObject inner = new JSONObject(innerJsonString);

        String JSonString = String.valueOf(ScheduleTaskService.getInstance().findTaskByName(paramString2));
        FineLoggerFactory.getLogger().error(JSonString);
        //{"id":"8a6a4cdc-a510-4827-b27f-74199dbb53f9","taskName":"test","taskDescription":"WorkBook1.cpt","templatePath":"WorkBook1.cpt","repeatTime":0,"repeatTimes":0,"showType":0,"taskType":1,"taskCondition":{"type":0,"description":"","detail":"Dec-Task_Run_Always"},"fileClearCount":1,"nextFireTime":null,"preFireTime":"2025-04-24 07:17:14","triggerGroup":{"triggers":[{"triggerType":"1","startTime":null,"endTime":null,"startType":1,"endType":1}]},"userGroup":{"userType":"1","departmentAndPost":[],"customRole":[],"departmentStr":"","customRoleStr":"","platformUser":["admin(admin)"],"platformUserStr":""},"creator":"admin","editable":false,"backupFilePath":"","sendBackupFile":false,"scheduleOutput":{"id":"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc","baseName":"","formats":[{"formatNum":0}],"createAttachByUsername":false,"outputActionList":[{"@class":"com.fr.schedule.base.bean.output.OutputSystemMsg","id":"4928f225-c8e2-4b29-acd5-07db2c05dde6","actionName":"com.fr.schedule.base.bean.output.OutputSystemMsg","resultURL":"http://localhost:8075/webroot/decision","outputId":"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc","subject":"g","content":"g","linkOpenType":1}],"baseNameMap":{},"formatsMap":{},"showTypesMap":{}},"taskState":{"value":2},"taskParameter":{"param":[]},"outputStr":"3,","conditionParameter":{"param":[]},"labels":null,"originTaskId":"","stopOnError":false,"scheduleTaskExtend":null,"hosts":[]}
        JSONObject jObject = new JSONObject(JSonString);
        Object VV = jObject.opt("id");
        if(VV == null ){
            return Response.error("201","Task Name을 정확이 기재해주세요. \n" + paramString2);
        }else{
            FineLoggerFactory.getLogger().error("ttt");
        }

        this.St1(paramHttpServletRequest,jObject.get("id").toString());
        return Response.ok("실행완료!");
    }

    /* Task Name을 통해, ID 찾기 */
    @RequestMapping(value = {"/task/{taskName}/v1"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response tassksksksksk(HttpServletRequest paramHttpServletRequest, @FinePathVariable("taskName") String paramString2) throws Exception {
        return Response.ok(String.valueOf(ScheduleTaskService.getInstance().findTaskByName(paramString2)));
    }

    /* Task Name을 통해, ID 찾기 */
    @RequestMapping(value = {"/task/{taskId}/v2"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response tassksksksfksk(@FinePathVariable("taskId") String paramString2) throws Exception {
        St2(paramString2);
        return Response.ok("good");
    }


    /* Task Name을 통해, ID 찾기 */
    @RequestMapping(value = {"/task/{taskId}/v3"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response tassksksksfkhk(@FinePathVariable("taskId") String paramString2) throws Exception {
        St3("admin",paramString2);
        return Response.ok("good");
    }

    public void St2 (String paramString2){
        MetricRegistry.getMetric().submit(OperateMessage.build("Dec-Module-Simple_Scheduler", "Fine-Schedule_Task", paramString2, "Dec-Log_Execute"));
    }
    public void St1 (HttpServletRequest paramHttpServletRequest,String paramString2) throws Exception {
        ScheduleTaskService.getInstance().onceStartTask(UserService.getInstance().getCurrentUserId(paramHttpServletRequest), paramString2);
    }
    public void St3(String aa , String bb) throws Exception {
        ScheduleTaskService.getInstance().onceStartTask("admin","test");
    }


    /*    private static PythonInterpreter intPre;

  @RequestMapping(value = "/py", method = RequestMethod.GET)
    @ResponseBody
        public String test (){
        System.setProperty("python.import.site","false");
        intPre = new PythonInterpreter();
        intPre.execfile("/webroot/test.py");
        PyFunction pyFuntion = (PyFunction) intPre.get("testFunc", PyFunction.class);
        int a = 10, b = 20;
        PyObject pyobj = pyFuntion.__call__(new PyInteger(a), new PyInteger(b));
        FineLoggerFactory.getLogger().error("py호출 === " + pyobj.toString());
        return pyobj.toString();
        }



    @RequestMapping(value = "/py2", method = RequestMethod.GET)
    @ResponseBody
    public String test2 (){
//        System.setProperty("python.import.site","false");
        return "test";
    }*/
  @RequestMapping(value = "/sso_ds_test", method = RequestMethod.GET)
  @ResponseBody
  public String ssotest(
          HttpServletResponse response,HttpServletRequest request
  ) throws IOException,Exception {
      String ssoServer = null ;
      String tt = null ;
      FineLoggerFactory.getLogger().error("1111");
      try {
          FineLoggerFactory.getLogger().error("22222");
           Common common = new Common();
          if(common == null) common = new Common();
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> parsedJson = objectMapper.readValue(common.getPropertyJson("conf/sso.properties"), Map.class);
          ssoServer = parsedJson.get("sso.url").toString();
          FineLoggerFactory.getLogger().error("33333");
          FineLoggerFactory.getLogger().error("sso.url===" + ssoServer);
      } catch (Exception e) {

          e.printStackTrace();
      }
      return ssoServer;
  }


    @RequestMapping(value = "/sso_ds", method = RequestMethod.GET)
    @ResponseBody
    public void ssot(
            @RequestParam("iv") String iv,
            @RequestParam("salt") String salt,
            @RequestParam("deviceUID") String deviceUID,
            @RequestParam("empNo") String encryptedData,
            HttpServletResponse response,HttpServletRequest request
    ) throws IOException,Exception {
        String tt = null ;
        try {
            LoginConfig lc = new LoginConfig();
            tt = lc.decrypt(encryptedData, iv, salt, deviceUID);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return tt;
        loginAction(tt,request,response);
    }

    public void loginAction(String username, HttpServletRequest request, HttpServletResponse response) throws IOException,Exception {
        boolean needLogin = true;
        Common common = new Common();
        if(common == null) common = new Common();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parsedJson = objectMapper.readValue(common.getPropertyJson("conf/sso.properties"), Map.class);
        //WEB-INF/clasess/conf/.... 파일안에 넣어야댑니다.
        String userNotExistsUrl = parsedJson.get("userNotExists.url").toString();
        String templateRenderUrl = parsedJson.get("template.render").toString();
        String ssoRediectUrl = parsedJson.get("sso.url").toString();

        FineLoggerFactory.getLogger().error("template.url===" + templateRenderUrl);
        FineLoggerFactory.getLogger().error("userNotExists.url===" + userNotExistsUrl);
        FineLoggerFactory.getLogger().error("ssoRediectUrl.url===" + ssoRediectUrl);
        String decision = null;
        try {
            decision = TemplateUtils.render(templateRenderUrl);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        try {
            User user = UserService.getInstance().getUserByUserName(username); //유저정보 취득
            /*유저정보 없을 경우 추가 사항*/
            if (user == null) {
                try {
                    response.sendRedirect(userNotExistsUrl);
//                    LoginService.getInstance().CommLogin(request, response, userBean.getUsername());
                } catch(Exception e){
                    response.sendRedirect(userNotExistsUrl);
                } finally{
                    response.sendRedirect(userNotExistsUrl);
                }
            }
            Device device = NetworkHelper.getDevice(request);
            /*자동 로그인을 위한 토큰 확인
            String oldToken = TokenResource.COOKIE.getToken(request);
;
            if (StringUtils.isEmpty(oldToken)) {
                needLogin = true;
            } else {
                try {
                    if (!ComparatorUtils.equals(username, JwtUtils.parseJWT(oldToken).getSubject())) {
                        FineLoggerFactory.getLogger().info("username changed：" + username);
                    }
                    LoginService.getInstance().loginStatusValid(oldToken, TerminalHandler.getTerminal(request, device));
                } catch (Exception e) {
                    needLogin = true;
                }
            }
*/

            if (needLogin) {
                TerminalHandler terminal = TerminalHandler.getTerminal(request, device);
                AuthenticController authenticController = ControllerFactory.getInstance().getAuthenticController(user.getId());
                long tokenTimeout = FSConfig.getInstance().getLoginConfig().getLoginTimeout();

                //private 메소드 호출을 위한 reflection api 추가를 통한 Token GET
                Method method = LoginService.class.getDeclaredMethod("generateToken", String.class, String.class, String.class,
                        long.class);
                method.setAccessible(true);
                String token = (String) method.invoke(LoginService.class.newInstance(), user.getUserName(), user.getDisplayName(),
                        user.getTenantId(), tokenTimeout);

                //중복로그인 체크
                authenticController.verifySingleLoginStatus(user.getUserName(), terminal, token);
                authenticController.logoutSingleLoginInvalidUser(user.getUserName(), terminal);

                //로그인처리
                LoginService.getInstance().login(request, response, username);
            }
            //로그인후 포탈로 이동
            response.sendRedirect(ssoRediectUrl);

            return;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("get ERROR===== ");
            FineLoggerFactory.getLogger().error(String.valueOf(e));
            e.printStackTrace();
        }

    }
}