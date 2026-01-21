package com.skala.springbootsample.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import com.skala.springbootsample.service.RegionService;
import com.skala.springbootsample.service.UserService;
import com.skala.springbootsample.domain.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Spring AI MCP Tools - 지역 관리 도구들
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegionMcpTools {

    private final RegionService regionService;
    private final UserService userService;

    @Tool(description = "지역 목록을 조회합니다.")
    public String getRegions() {
        log.info("MCP Tool 호출: getRegions");

        try {
            List<Region> regions = regionService.findAll();

            if (regions.isEmpty()) {
                return "등록된 지역이 없습니다.";
            }

            StringBuilder result = new StringBuilder();
            result.append("등록된 지역: ").append(regions.size()).append("개\n\n");

            for (Region region : regions) {
                result.append("- ID: ").append(region.getId())
                        .append(", 이름: ").append(region.getName())
                        .append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            log.error("지역 조회 중 오류", e);
            return "지역 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "특정 ID의 지역 상세 정보를 조회합니다.")
    public String getRegionById(
            @ToolParam(description = "조회할 지역의 고유 ID", required = true) 
            long regionId) {
        log.info("MCP Tool 호출: getRegionById, regionId={}", regionId);

        try {
            Optional<Region> regionOpt = regionService.findById(regionId);

            if (regionOpt.isEmpty()) {
                return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
            }

            Region region = regionOpt.get();
            long userCount = userService.findByRegionId(regionId).size();

            return String.format("""
                지역 정보:
                - ID: %d
                - 이름: %s
                - 등록된 사용자 수: %d명
                """,
                    region.getId(), region.getName(), userCount);

        } catch (Exception e) {
            log.error("지역 조회 중 오류", e);
            return "지역 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "지역명으로 지역을 조회합니다.")
    public String getRegionByName(
            @ToolParam(description = "검색할 지역의 이름", required = true) 
            String regionName) {
        log.info("MCP Tool 호출: getRegionByName, regionName={}", regionName);

        try {
            if (regionName == null || regionName.trim().isEmpty()) {
                return "지역명을 입력해주세요.";
            }

            Optional<Region> regionOpt = regionService.findByName(regionName.trim());

            if (regionOpt.isEmpty()) {
                return "'" + regionName + "'인 지역을 찾을 수 없습니다.";
            }

            Region region = regionOpt.get();
            long userCount = userService.findByRegionId(region.getId()).size();

            return String.format("""
                지역 정보:
                - ID: %d
                - 이름: %s
                - 등록된 사용자 수: %d명
                """,
                    region.getId(), region.getName(), userCount);

        } catch (Exception e) {
            log.error("지역 조회 중 오류", e);
            return "지역 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "새 지역을 생성합니다.")
    public String createRegion(
            @ToolParam(description = "생성할 지역의 이름", required = true) 
            String regionName) {
        log.info("MCP Tool 호출: createRegion, regionName={}", regionName);

        try {
            if (regionName == null || regionName.trim().isEmpty()) {
                return "지역 이름은 필수입니다.";
            }

            Region region = new Region(regionName.trim());
            Region created = regionService.create(region);

            return String.format("지역이 성공적으로 생성되었습니다: %s (ID: %d)",
                    created.getName(), created.getId());

        } catch (IllegalArgumentException e) {
            log.warn("지역 생성 실패: {}", e.getMessage());
            return "지역 생성 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("지역 생성 중 오류", e);
            return "지역 생성 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "지역 정보를 수정합니다.")
    public String updateRegion(
            @ToolParam(description = "수정할 지역의 고유 ID", required = true) 
            long regionId, 
            @ToolParam(description = "새로운 지역 이름", required = true) 
            String regionName) {
        log.info("MCP Tool 호출: updateRegion, regionId={}, regionName={}", regionId, regionName);

        try {
            if (regionName == null || regionName.trim().isEmpty()) {
                return "지역 이름은 필수입니다.";
            }

            // 지역 존재 확인
            Optional<Region> regionOpt = regionService.findById(regionId);
            if (regionOpt.isEmpty()) {
                return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
            }

            Region updateRegion = new Region();
            updateRegion.setName(regionName.trim());

            Optional<Region> updatedOpt = regionService.update(regionId, updateRegion);
            if (updatedOpt.isEmpty()) {
                return "지역 수정에 실패했습니다.";
            }

            Region updated = updatedOpt.get();
            return String.format("지역이 성공적으로 수정되었습니다: %s (ID: %d)",
                    updated.getName(), updated.getId());

        } catch (IllegalArgumentException e) {
            log.warn("지역 수정 실패: {}", e.getMessage());
            return "지역 수정 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("지역 수정 중 오류", e);
            return "지역 수정 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "지역을 삭제합니다.")
    public String deleteRegion(
            @ToolParam(description = "삭제할 지역의 고유 ID", required = true) 
            long regionId) {
        log.info("MCP Tool 호출: deleteRegion, regionId={}", regionId);

        try {
            // 지역 존재 확인
            Optional<Region> regionOpt = regionService.findById(regionId);
            if (regionOpt.isEmpty()) {
                return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
            }

            // 해당 지역에 사용자가 있는지 확인
            long userCount = userService.findByRegionId(regionId).size();
            if (userCount > 0) {
                return "해당 지역에 " + userCount + "명의 사용자가 등록되어 있어 삭제할 수 없습니다.";
            }

            boolean deleted = regionService.delete(regionId);
            if (deleted) {
                return "지역이 성공적으로 삭제되었습니다: " + regionOpt.get().getName();
            } else {
                return "지역 삭제에 실패했습니다.";
            }

        } catch (Exception e) {
            log.error("지역 삭제 중 오류", e);
            return "지역 삭제 중 오류 발생: " + e.getMessage();
        }
    }
}
