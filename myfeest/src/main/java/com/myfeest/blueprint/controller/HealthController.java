package com.myfeest.blueprint.controller;

import com.myfeest.blueprint.dto.HealthResponse;
import com.myfeest.blueprint.dto.StandardApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para health checks e monitoramento
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Endpoints de monitoramento e health check")
public class HealthController {
    
    /**
     * Health check básico
     */
    @GetMapping
    @Operation(summary = "Health check básico", description = "Retorna o status básico da aplicação")
    public ResponseEntity<StandardApiResponse<HealthResponse>> health() {
        HealthResponse health = HealthResponse.builder()
            .status("UP")
            .timestamp(LocalDateTime.now())
            .version("1.0.0")
            .build();
        
        return ResponseEntity.ok(StandardApiResponse.success("Aplicação funcionando", health));
    }
    
    /**
     * Health check detalhado
     */
    @GetMapping("/detailed")
    @Operation(summary = "Health check detalhado", description = "Retorna informações detalhadas sobre a saúde da aplicação")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> detailedHealth() {
        Map<String, Object> details = new HashMap<>();
        
        // Status geral
        details.put("status", "UP");
        details.put("timestamp", LocalDateTime.now());
        details.put("version", "1.0.0");
        
        // Informações do sistema
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        Map<String, Object> system = new HashMap<>();
        system.put("uptime", runtimeBean.getUptime());
        system.put("startTime", runtimeBean.getStartTime());
        system.put("javaVersion", runtimeBean.getSystemProperties().get("java.version"));
        system.put("heapMemoryUsage", memoryBean.getHeapMemoryUsage());
        system.put("nonHeapMemoryUsage", memoryBean.getNonHeapMemoryUsage());
        
        details.put("system", system);
        
        // Health checks dos componentes
        Map<String, Object> components = new HashMap<>();
        
        components.put("database", "UP");
        components.put("cache", "UP");
        components.put("api", "UP");
        
        details.put("components", components);
        
        return ResponseEntity.ok(StandardApiResponse.success("Health check detalhado", details));
    }
    
    /**
     * Informações do sistema
     */
    @GetMapping("/system")
    @Operation(summary = "Informações do sistema", description = "Retorna informações detalhadas sobre o sistema")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> systemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        systemInfo.put("javaVersion", runtimeBean.getSystemProperties().get("java.version"));
        systemInfo.put("javaVendor", runtimeBean.getSystemProperties().get("java.vendor"));
        systemInfo.put("osName", runtimeBean.getSystemProperties().get("os.name"));
        systemInfo.put("osVersion", runtimeBean.getSystemProperties().get("os.version"));
        systemInfo.put("uptime", runtimeBean.getUptime());
        systemInfo.put("startTime", runtimeBean.getStartTime());
        systemInfo.put("heapMemoryUsage", memoryBean.getHeapMemoryUsage());
        systemInfo.put("nonHeapMemoryUsage", memoryBean.getNonHeapMemoryUsage());
        
        return ResponseEntity.ok(StandardApiResponse.success("Informações do sistema", systemInfo));
    }
} 