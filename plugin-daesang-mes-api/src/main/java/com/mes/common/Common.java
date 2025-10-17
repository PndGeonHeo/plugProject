package com.mes.common;

import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

@Controller
@RequestMapping(value = "/{svc}")
@LoginStatusChecker(required = false)
public class Common {
    private static final String SQL_FILENAME = "sqlmap.xml";
    private static Common commonInstance = null;
    @Autowired
    private ServiceContext serviceContext;

    public Common() {
    }

    // Common 객체 반환 (싱글톤 패턴 사용)
    public static Common getInstance() {
        if (commonInstance == null) {
            synchronized (Common.class) {
                if (commonInstance == null) {
                    commonInstance = new Common(); // 객체가 없으면 생성
                }
            }
        }
        return commonInstance;
    }

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public Common(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }


    /*
        get properties
        로컬 환경: 프로젝트폴더\build\classes 하위
    */
    public static Properties getProperties(String path) {
        Properties props = new Properties();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) cl = ClassLoader.getSystemClassLoader();

            URL url = cl.getResource(path);

            if (url == null) {
                throw new Exception("properties not exist. path=" + path);
            }

            String filepath = url.getFile();
            props.load(new InputStreamReader(new FileInputStream(filepath)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }

    /*
        get property in path
    */
    public static String getProperty(String path, String name) {
        String result = "";

        try {
            Properties props = getProperties(path);
            result = props.getProperty(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}