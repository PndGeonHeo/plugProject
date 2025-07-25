import com.common.develop.CommonJsonUtil;
import com.fr.decision.authority.data.User;
import com.fr.decision.service.DecisionServiceManager;
import com.fr.decision.service.authority.DecisionUserServiceProvider;
import com.fr.decision.service.system.DecisionMessageServiceProvider;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.log.FineLoggerFactory;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
public class propertiesT {

    public static final String alg = "AES/CBC/PKCS5Padding";
    public static final String _256key = "/CU01dQ69b6xwIYgpR5vWIU40ugJZNRlGTrQgRjU454=";
    public  static final String iv = _256key.substring(0, 16); // 16byte

    public static String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        byte[] key = Base64.getDecoder().decode(_256key);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        byte[] key = Base64.getDecoder().decode(_256key);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }



    public static String getMapValue(Map<String,Object> map,String mapKey) throws IOException {
        // Map에서 필요한 키값을 추출하여 반환
        if (map.containsKey(mapKey)) {
            return map.get(mapKey).toString();
        } else {
            throw new IllegalArgumentException("Property key " + mapKey );
        }
    }



    // 문자열을 SHA-256 해시로 변환
    public static String sha256encrypt(String input) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        // MessageDigest 객체 생성
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // 입력 데이터를 바이트 배열로 변환하여 해시 생성
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // 바이트 배열을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b); // 바이트를 16진수로 변환
            if (hex.length() == 1) {
                hexString.append('0'); // 1자리일 경우 앞에 '0' 추가
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //사용자 PW GET
    public static String loginPw(String username) throws Exception{
        DecisionUserServiceProvider userservice = DecisionServiceManager.getInstance().getService(DecisionUserServiceProvider.class);
        DecisionMessageServiceProvider messageService = DecisionServiceManager.getInstance().getService(DecisionMessageServiceProvider.class);
        User user = userservice.getUserByUserName(username); //유저정보 취득
        return user.getPassword() ;
    }



    public static void main(String[] args) throws Exception {
//        CommLogin login = new CommLogin();
//        Class clazz = ;


    //FineLoggerFactory.getLogger().error("{\"data\":\"{\\\"id\\\":\\\"8a6a4cdc-a510-4827-b27f-74199dbb53f9\\\",\\\"taskName\\\":\\\"test\\\",\\\"taskDescription\\\":\\\"WorkBook1.cpt\\\",\\\"templatePath\\\":\\\"WorkBook1.cpt\\\",\\\"repeatTime\\\":0,\\\"repeatTimes\\\":0,\\\"showType\\\":0,\\\"taskType\\\":1,\\\"taskCondition\\\":{\\\"type\\\":0,\\\"description\\\":\\\"\\\",\\\"detail\\\":\\\"Dec-Task_Run_Always\\\"},\\\"fileClearCount\\\":1,\\\"nextFireTime\\\":null,\\\"preFireTime\\\":\\\"2025-04-24 07:17:14\\\",\\\"triggerGroup\\\":{\\\"triggers\\\":[{\\\"triggerType\\\":\\\"1\\\",\\\"startTime\\\":null,\\\"endTime\\\":null,\\\"startType\\\":1,\\\"endType\\\":1}]},\\\"userGroup\\\":{\\\"userType\\\":\\\"1\\\",\\\"departmentAndPost\\\":[],\\\"customRole\\\":[],\\\"departmentStr\\\":\\\"\\\",\\\"customRoleStr\\\":\\\"\\\",\\\"platformUser\\\":[\\\"admin(admin)\\\"],\\\"platformUserStr\\\":\\\"\\\"},\\\"creator\\\":\\\"admin\\\",\\\"editable\\\":false,\\\"backupFilePath\\\":\\\"\\\",\\\"sendBackupFile\\\":false,\\\"scheduleOutput\\\":{\\\"id\\\":\\\"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc\\\",\\\"baseName\\\":\\\"\\\",\\\"formats\\\":[{\\\"formatNum\\\":0}],\\\"createAttachByUsername\\\":false,\\\"outputActionList\\\":[{\\\"@class\\\":\\\"com.fr.schedule.base.bean.output.OutputSystemMsg\\\",\\\"id\\\":\\\"4928f225-c8e2-4b29-acd5-07db2c05dde6\\\",\\\"actionName\\\":\\\"com.fr.schedule.base.bean.output.OutputSystemMsg\\\",\\\"resultURL\\\":\\\"http://localhost:8075/webroot/decision\\\",\\\"outputId\\\":\\\"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc\\\",\\\"subject\\\":\\\"g\\\",\\\"content\\\":\\\"g\\\",\\\"linkOpenType\\\":1}],\\\"baseNameMap\\\":{},\\\"formatsMap\\\":{},\\\"showTypesMap\\\":{}},\\\"taskState\\\":{\\\"value\\\":2},\\\"taskParameter\\\":{\\\"param\\\":[]},\\\"outputStr\\\":\\\"3,\\\",\\\"conditionParameter\\\":{\\\"param\\\":[]},\\\"labels\\\":null,\\\"originTaskId\\\":\\\"\\\",\\\"stopOnError\\\":false,\\\"scheduleTaskExtend\\\":null,\\\"hosts\\\":[]}\"}");
    String jsonString = "{\"data\":\"{\\\"id\\\":\\\"8a6a4cdc-a510-4827-b27f-74199dbb53f9\\\",\\\"taskName\\\":\\\"test\\\",\\\"taskDescription\\\":\\\"WorkBook1.cpt\\\",\\\"templatePath\\\":\\\"WorkBook1.cpt\\\",\\\"repeatTime\\\":0,\\\"repeatTimes\\\":0,\\\"showType\\\":0,\\\"taskType\\\":1,\\\"taskCondition\\\":{\\\"type\\\":0,\\\"description\\\":\\\"\\\",\\\"detail\\\":\\\"Dec-Task_Run_Always\\\"},\\\"fileClearCount\\\":1,\\\"nextFireTime\\\":null,\\\"preFireTime\\\":\\\"2025-04-24 07:17:14\\\",\\\"triggerGroup\\\":{\\\"triggers\\\":[{\\\"triggerType\\\":\\\"1\\\",\\\"startTime\\\":null,\\\"endTime\\\":null,\\\"startType\\\":1,\\\"endType\\\":1}]},\\\"userGroup\\\":{\\\"userType\\\":\\\"1\\\",\\\"departmentAndPost\\\":[],\\\"customRole\\\":[],\\\"departmentStr\\\":\\\"\\\",\\\"customRoleStr\\\":\\\"\\\",\\\"platformUser\\\":[\\\"admin(admin)\\\"],\\\"platformUserStr\\\":\\\"\\\"},\\\"creator\\\":\\\"admin\\\",\\\"editable\\\":false,\\\"backupFilePath\\\":\\\"\\\",\\\"sendBackupFile\\\":false,\\\"scheduleOutput\\\":{\\\"id\\\":\\\"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc\\\",\\\"baseName\\\":\\\"\\\",\\\"formats\\\":[{\\\"formatNum\\\":0}],\\\"createAttachByUsername\\\":false,\\\"outputActionList\\\":[{\\\"@class\\\":\\\"com.fr.schedule.base.bean.output.OutputSystemMsg\\\",\\\"id\\\":\\\"4928f225-c8e2-4b29-acd5-07db2c05dde6\\\",\\\"actionName\\\":\\\"com.fr.schedule.base.bean.output.OutputSystemMsg\\\",\\\"resultURL\\\":\\\"http://localhost:8075/webroot/decision\\\",\\\"outputId\\\":\\\"70d0be9b-a7d7-4b07-9aa8-704ea34b50fc\\\",\\\"subject\\\":\\\"g\\\",\\\"content\\\":\\\"g\\\",\\\"linkOpenType\\\":1}],\\\"baseNameMap\\\":{},\\\"formatsMap\\\":{},\\\"showTypesMap\\\":{}},\\\"taskState\\\":{\\\"value\\\":2},\\\"taskParameter\\\":{\\\"param\\\":[]},\\\"outputStr\\\":\\\"3,\\\",\\\"conditionParameter\\\":{\\\"param\\\":[]},\\\"labels\\\":null,\\\"originTaskId\\\":\\\"\\\",\\\"stopOnError\\\":false,\\\"scheduleTaskExtend\\\":null,\\\"hosts\\\":[]}\"}";
        JSONObject outuer = new JSONObject(jsonString);

        FineLoggerFactory.getLogger().error(outuer.get("data").toString());
        JSONObject inner = new JSONObject(outuer.get("data").toString());
        FineLoggerFactory.getLogger().error(inner.get("id").toString());
        Object VV = inner.opt("id");
        if(VV == null ){
            FineLoggerFactory.getLogger().error("없음");
        }else{
            FineLoggerFactory.getLogger().error("ttt");
        }
        //FineLoggerFactory.getLogger().error();


        FineLoggerFactory.getLogger().error(String.valueOf(UserService.getInstance().getAdminUserList()));

        Map<String,Object> test = new HashMap<String,Object>();
        test.put("test","11");
        test.put("test2","22");
        FineLoggerFactory.getLogger().error(encrypt("admin"));
        FineLoggerFactory.getLogger().error(getMapValue(test,"test"));
//        FineLoggerFactory.getLogger().error(encrypt("test"));
//        FineLoggerFactory.getLogger().error(encrypt(iv));
//        FineLoggerFactory.getLogger().error(sha256encrypt("test"));
//
//        FineLoggerFactory.getLogger().error(decrypt(encrypt("test")));
//        FineLoggerFactory.getLogger().error(sha256encrypt(decrypt(encrypt("test"))));
//
//        FineLoggerFactory.getLogger().error(loginPw("admin"));

        String aa ="{\"username\":\"admin\",\"password\":\"8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918\"}";

        String aaa = CommonJsonUtil.getStringFromJsonObject(CommonJsonUtil.parseJsonObject(aa),"username");
        FineLoggerFactory.getLogger().error(aaa);





//        Common common = new Common();
//        if (common == null) common = new Common();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        String jsonss = common.getPropertyJsonTree("conf/CommLogin.properties", CommLogin.class);
//        FineLoggerFactory.getLogger().error(jsonss);
//
//
//        if(common == null) common = new Common();
//        Map<String, Object> parsedJson = objectMapper.readValue(common.getPropertyJson("conf/CommLogin.properties", CommLogin.class), Map.class);
//        FineLoggerFactory.getLogger().error("tt===" + parsedJson.toString() );

//        int i= 0, sum = 0 ;
//        while(i < 10) {
//            FineLoggerFactory.getLogger().error("===========================");
//            FineLoggerFactory.getLogger().error("start I ===" + i);
//            i++;
//            FineLoggerFactory.getLogger().error("start I ++  ===" + i);
//            FineLoggerFactory.getLogger().error(String.valueOf("참 거짓!!!! " + (i % 2 == 1)));
//            if (i % 2 == 1)
//                continue;
//            sum += i;
//            FineLoggerFactory.getLogger().error("SUM I ===" + sum);
//            FineLoggerFactory.getLogger().error("===========================");
//        }
//        FineLoggerFactory.getLogger().error(String.valueOf(sum));
//        }
        //        Common common = new Common();
//        FineLoggerFactory.getLogger().error(Common.getPropertyJson("conf/ftp.properties"));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            // JSON 문자열을 Map으로 파싱
//            Map<String, Object> parsedJson = objectMapper.readValue(Common.getPropertyJson("conf/ftp.properties"), Map.class);
//            // 파싱된 JSON 출력
//            System.out.println("Message: " + parsedJson.get("message").toString());
//            System.out.println("Code: " + parsedJson.get("code").toString());
//            System.out.println("port: " + Integer.parseInt(parsedJson.get("ftp.port").toString()));
//        }catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//    }
//https://dscext.daesang.com:8413/webroot/decision/sso_ds?empNo=yF75kN0YHzHs8QiV31k81w==&iv=2b14c034b3b239f56a0d3f0c941af2e9&salt=9afd6eb7542fdb1aa410767b5025ab46&deviceUID=d7f610a3a970eb7d
//
//
//
//
///---------------------

        /*
         *
         * empNo=Lq%2BE1bReUVWKcrff79Jhdg%3D%3D
         * &iv=40a7b886c0fda9162f3e874c6c925f93
         * &salt=4ae4f8ddc857b91da454920fa80878c7
         * &deviceUID=d7f610a3a970eb7d
         *  HTTP/1.1" 400 2681
         *
         * */
//        String tt = null ;
//        LoginConfig lc = new LoginConfig();
        //AS-IS
//        tt = lc.decrypt("yF75kN0YHzHs8QiV31k81w==", "2b14c034b3b239f56a0d3f0c941af2e9", "9afd6eb7542fdb1aa410767b5025ab46", "d7f610a3a970eb7d");
        //TO-BE
//        tt = lc.decrypt("Lq%2BE1bReUVWKcrff79Jhdg==", "40a7b886c0fda9162f3e874c6c925f93", "4ae4f8ddc857b91da454920fa80878c7", "d7f610a3a970eb7d");
//        FineLoggerFactory.getLogger().error(tt);
    }
}




