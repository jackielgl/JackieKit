package com.jackie.ftp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 从配置文件里面获取值 通过key来获取value
 * @author Administrator
 *
 */
public class PropertiesUtil {
    private static Logger logger = LogManager.getLogger(PropertiesUtil.class);
    private static Properties properties;
    private static final String name="application.properties";
    static {
        try {
            properties=new Properties();
            properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(name), "utf-8"));
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
            logger.error("IOException"+e);
        }
    }

    public static String  getValue(String key) {
        String value=properties.getProperty(key).trim();
        if(value==null) {
            return	null;
        }
        return value;
    }


    public static String  getValue(String key,String defaultvalue) {
        String value=properties.getProperty(key).trim();
        if(value==null) {
            value=defaultvalue;
        }
        return value;
    }
}
