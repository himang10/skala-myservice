package com.skala.springbootsample.config;

import java.sql.Date;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skala.springbootsample.mcp.DateTimeTools;
import com.skala.springbootsample.mcp.WeatherTools;
import com.skala.springbootsample.mcp.RegionMcpTools;
import com.skala.springbootsample.mcp.SystemMcpTools;
import com.skala.springbootsample.mcp.UserMcpTools;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring AI MCP 서버 설정
 */
@Slf4j
@Configuration
public class McpConfiguration {

    @Bean
    public ToolCallbackProvider commonMcpToolProvider(
            UserMcpTools userMcpTools,
            RegionMcpTools regionMcpTools,
            SystemMcpTools systemMcpTools,
            DateTimeTools dateTimeTools,
            WeatherTools weatherTools
    ) {
        log.info("Common MCP Tool Callback Provider 설정 중...");   
        return MethodToolCallbackProvider.builder()
                .toolObjects(userMcpTools, regionMcpTools, systemMcpTools, dateTimeTools, weatherTools)
                .build();   
    }

}
