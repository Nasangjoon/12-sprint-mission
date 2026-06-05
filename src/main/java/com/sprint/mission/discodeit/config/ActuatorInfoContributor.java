package com.sprint.mission.discodeit.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActuatorInfoContributor implements InfoContributor {

    private final Environment environment;

    public ActuatorInfoContributor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void contribute(Info.Builder builder) {
        // 애플리케이션 정보
        builder.withDetail("app", Map.of(
            "name", "Discodeit",
            "version", "1.7.0"
        ));

        // 자바 및 스프링 부트 버전 정보
        builder.withDetail("java", Map.of(
            "version", "17"
        ));
        
        builder.withDetail("spring-boot", Map.of(
            "version", "3.4.0"
        ));

        // 주요 설정 정보 (config)
        Map<String, Object> config = new HashMap<>();

        // 데이터소스 정보
        Map<String, Object> datasource = new HashMap<>();
        datasource.put("url", environment.getProperty("spring.datasource.url"));
        datasource.put("driver-class-name", environment.getProperty("spring.datasource.driver-class-name"));
        config.put("datasource", datasource);

        // JPA 설정 정보
        Map<String, Object> jpa = new HashMap<>();
        jpa.put("ddl-auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        config.put("jpa", jpa);

        // Storage 설정 정보
        Map<String, Object> storage = new HashMap<>();
        storage.put("type", environment.getProperty("discodeit.storage.type"));
        storage.put("path", environment.getProperty("discodeit.storage.local.root-path"));
        config.put("storage", storage);

        // Multipart 설정 정보
        Map<String, Object> multipart = new HashMap<>();
        multipart.put("max-file-size", environment.getProperty("spring.servlet.multipart.max-file-size"));
        if (multipart.get("max-file-size") == null) {
            multipart.put("max-file-size", environment.getProperty("spring.servlet.multipart.maxFileSize"));
        }
        multipart.put("max-request-size", environment.getProperty("spring.servlet.multipart.max-request-size"));
        if (multipart.get("max-request-size") == null) {
            multipart.put("max-request-size", environment.getProperty("spring.servlet.multipart.maxRequestSize"));
        }
        config.put("multipart", multipart);

        builder.withDetail("config", config);
    }
}
