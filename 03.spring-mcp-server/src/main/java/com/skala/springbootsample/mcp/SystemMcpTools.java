package com.skala.springbootsample.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import com.skala.springbootsample.service.UserService;
import com.skala.springbootsample.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Spring AI MCP Tools - 시스템 관리 도구들
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemMcpTools {

    private final UserService userService;
    private final RegionService regionService;

    @Tool(description = "시스템 상태 정보를 조회합니다.")
    public String getSystemStatus() {
        log.info("MCP Tool 호출: getSystemStatus");

        try {
            long userCount = userService.findAll(Optional.empty()).size();
            long regionCount = regionService.findAll().size();

            return String.format("""
                시스템 상태:
                - 총 사용자 수: %d명
                - 총 지역 수: %d개
                - 서버 상태: 정상
                - 현재 시간: %s
                """,
                    userCount, regionCount, java.time.LocalDateTime.now());

        } catch (Exception e) {
            log.error("시스템 상태 조회 중 오류", e);
            return "시스템 상태 조회 중 오류 발생: " + e.getMessage();
        }
    }
}
