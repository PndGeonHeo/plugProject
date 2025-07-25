package com.controller.develop;

import com.common.develop.Common;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.log.FineLoggerFactory;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.RequestMapping;
import com.fr.third.springframework.web.bind.annotation.RequestMethod;
import com.fr.third.springframework.web.bind.annotation.RequestParam;
import com.fr.third.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@LoginStatusChecker(required = false)
public class ftp {


    @RequestMapping(value = "/pp", method = RequestMethod.GET)
    @ResponseBody
    public String properties(@RequestParam("path")String path) {
        return Common.getPropertyJson(path);
    }

        @RequestMapping(value = "/pp2", method = RequestMethod.GET)
    @ResponseBody
    public void properties2(){
//        FineLoggerFactory.getLogger().error("ftpserver==" + ftpserver);
//        FineLoggerFactory.getLogger().error("URL==" + URL);

    }


    @RequestMapping(value = "/ftp_dn", method = RequestMethod.GET)
    @ResponseBody
    public void ftpDownLoad(
            @RequestParam("FP") String FP,
            @RequestParam("FN") String FN,
            HttpServletResponse response,HttpServletRequest request
    ) throws IOException {
        Common common = new Common();
        if(common == null) common = new Common();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parsedJson = objectMapper.readValue(common.getPropertyJson("conf/ftp.properties"), Map.class);
        String server = parsedJson.get("ftp.server").toString();
        int port = Integer.parseInt(parsedJson.get("ftp.port").toString()); // 기본 FTP 포트
        String user = parsedJson.get("ftp.user").toString();
        String pass = parsedJson.get("ftp.pw").toString();
        FineLoggerFactory.getLogger().error("user===" + user);
        FineLoggerFactory.getLogger().error("pass===" + pass);
        FineLoggerFactory.getLogger().error("000000");
        FTPClient ftpClient = new FTPClient();
        try {
//            FineLoggerFactory.getLogger().error("0000000");
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String remoteFile = FP+ "/" + FN;
            String localFile = parsedJson.get("ftp.localFile").toString()+ FN;
            try (OutputStream outputStream = new FileOutputStream(localFile)) {
                boolean success = ftpClient.retrieveFile( new String(remoteFile.getBytes(parsedJson.get("ftp.target.encoding.F").toString()),parsedJson.get("ftp.target.encoding.S").toString()), outputStream);
                if (success) {
                    // 파일 다운로드 완료 후 클라이언트에 응답
                    response.setContentType("application/octet-stream");
                    String encodedFileName = URLEncoder.encode(FN, parsedJson.get("ftp.url.encoding").toString());
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
                    response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

                    response.setContentLength((int) new java.io.File(localFile).length());
                    try (java.io.FileInputStream in = new java.io.FileInputStream(localFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    FineLoggerFactory.getLogger().error("파일이없습니다.");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on the server.");
                }
            }
        } catch (IOException ex) {
            FineLoggerFactory.getLogger().error("다운로드중 오류가 발생했습니다.");
            FineLoggerFactory.getLogger().error(ex.toString());
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while downloading file.");
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                FineLoggerFactory.getLogger().error("Finally Error");
                ex.printStackTrace();
            }
        }

    }

}