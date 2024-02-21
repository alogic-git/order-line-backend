package com.orderline.basic.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("Env")
public class Env {

    private static EnvProperties envProperties;

    @Autowired
    public Env(EnvProperties envProperties) {
        Env.envProperties = envProperties;
    }

    public static String jwtSecret() {
        return envProperties.getJwtSecret();
    }

    public static String smsApiKey() {
        return envProperties.getSms().getApiKey();
    }

    public static String smsAccessKey() {
        return envProperties.getSms().getAccessKey();
    }

    public static String smsSecretKey() {
        return envProperties.getSms().getSecretKey();
    }

    public static String smsPhone() {
        return envProperties.getSms().getPhone();
    }

    @Component
    @ConfigurationProperties(prefix="env")
    @Getter
    @Setter
    private static class EnvProperties {
        private String env;

        private String jwtSecret;

        private Sms sms;

        @Data
        private static class Sms {
            private String apiKey;
            private String accessKey;
            private String secretKey;
            private String phone;
        }

    }
}
