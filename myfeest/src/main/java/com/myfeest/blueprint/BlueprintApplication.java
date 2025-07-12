package com.myfeest.blueprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Aplicação principal do Sistema de Blueprint
 * 
 * @author MyFeest Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@EnableAsync
public class BlueprintApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BlueprintApplication.class, args);
    }
} 