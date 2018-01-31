package com.laohe.env;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 读取系统配置工具类
 *
 * @author suny
 */
public class SystemConfigUtils {

    private static Log logger = LogFactory.getLog(SystemConfigUtils.class);

    private static CompositeConfiguration config;

    static {
        try {
            config = new CompositeConfiguration();
            Resource resource = new FileSystemResource(System.getenv("SL_CONFIG") + "/system_config.properties");
            config.addConfiguration(new PropertiesConfiguration(resource.getFile()));
        } catch (ConfigurationException | IOException e) {
            logger.error("读取系统配置文件有误：" + e.getMessage());
        }
    }

    public static String getString(String key) {

        return config.getString(key);
    }

    public static void main(String[] args) {
        String business_send = getString("p2p_balance_url");
        System.out.println(business_send);
    }
}
