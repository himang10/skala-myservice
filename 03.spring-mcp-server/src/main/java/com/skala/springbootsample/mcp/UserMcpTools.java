package com.skala.springbootsample.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import com.skala.springbootsample.service.UserService;
import com.skala.springbootsample.service.RegionService;
import com.skala.springbootsample.domain.User;
import com.skala.springbootsample.domain.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.List;

/**
 * Spring AI MCP Tools - 사용자 관리 도구들
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class  UserMcpTools {

    private final UserService userService;
    private final RegionService regionService;

    @Tool(description = "사용자 목록을 조회합니다. 이름으로 필터링할 수 있습니다.")
    public String getUsers(
            @ToolParam(description = "필터링할 사용자 이름 (선택사항, null이면 전체 조회)", required = false) 
            String name) {
        log.info("MCP Tool 호출: getUsers, name={}", name);

        try {
            List<User> users = userService.findAll(Optional.ofNullable(name));

            if (users.isEmpty()) {
                return name != null ?
                        "'" + name + "'과 일치하는 사용자가 없습니다." :
                        "등록된 사용자가 없습니다.";
            }

            StringBuilder result = new StringBuilder();
            result.append("조회된 사용자: ").append(users.size()).append("명\n\n");

            for (User user : users) {
                result.append("- ID: ").append(user.getId())
                        .append(", 이름: ").append(user.getName())
                        .append(", 이메일: ").append(user.getEmail())
                        .append(", 지역: ").append(user.getRegion().getName())
                        .append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            log.error("사용자 조회 중 오류", e);
            return "사용자 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "특정 ID의 사용자 상세 정보를 조회합니다.")
    public String getUserById(
            @ToolParam(description = "조회할 사용자의 고유 ID", required = true) 
            long userId) {
        log.info("MCP Tool 호출: getUserById, userId={}", userId);

        try {
            Optional<User> userOpt = userService.findById(userId);

            if (userOpt.isEmpty()) {
                return "ID " + userId + "인 사용자를 찾을 수 없습니다.";
            }

            User user = userOpt.get();
            return String.format("""
                사용자 정보:
                - ID: %d
                - 이름: %s
                - 이메일: %s
                - 지역: %s (ID: %d)
                """,
                    user.getId(), user.getName(), user.getEmail(),
                    user.getRegion().getName(), user.getRegion().getId());

        } catch (Exception e) {
            log.error("사용자 조회 중 오류", e);
            return "사용자 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "특정 지역의 사용자들을 조회합니다.")
    public String getUsersByRegion(
            @ToolParam(description = "조회할 지역의 고유 ID", required = true) 
            long regionId) {
        log.info("MCP Tool 호출: getUsersByRegion, regionId={}", regionId);

        try {
            // 지역이 존재하는지 먼저 확인
            Optional<Region> regionOpt = regionService.findById(regionId);
            if (regionOpt.isEmpty()) {
                return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
            }

            List<User> users = userService.findByRegionId(regionId);
            Region region = regionOpt.get();

            if (users.isEmpty()) {
                return region.getName() + " 지역에 등록된 사용자가 없습니다.";
            }

            StringBuilder result = new StringBuilder();
            result.append(region.getName()).append(" 지역 사용자: ").append(users.size()).append("명\n\n");

            for (User user : users) {
                result.append("- ").append(user.getName())
                        .append(" (").append(user.getEmail()).append(")")
                        .append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            log.error("지역별 사용자 조회 중 오류", e);
            return "지역별 사용자 조회 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "새 사용자를 생성합니다.")
    public String createUser(
            @ToolParam(description = "사용자의 이름", required = true) 
            String name, 
            @ToolParam(description = "사용자의 이메일 주소", required = true) 
            String email, 
            @ToolParam(description = "사용자가 속할 지역의 고유 ID", required = true) 
            long regionId) {
        log.info("MCP Tool 호출: createUser, name={}, email={}, regionId={}", name, email, regionId);

        try {
            // 입력 검증
            if (name == null || name.trim().isEmpty()) {
                return "사용자 이름은 필수입니다.";
            }
            if (email == null || email.trim().isEmpty()) {
                return "이메일은 필수입니다.";
            }

            // 지역 확인
            Optional<Region> regionOpt = regionService.findById(regionId);
            if (regionOpt.isEmpty()) {
                return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
            }

            Region region = regionOpt.get();
            User user = new User(name.trim(), email.trim(), region);
            User created = userService.create(user);

            return String.format("""
                사용자가 성공적으로 생성되었습니다:
                - ID: %d
                - 이름: %s
                - 이메일: %s
                - 지역: %s
                """,
                    created.getId(), created.getName(), created.getEmail(), created.getRegion().getName());

        } catch (IllegalArgumentException e) {
            log.warn("사용자 생성 실패: {}", e.getMessage());
            return "사용자 생성 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("사용자 생성 중 오류", e);
            return "사용자 생성 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "사용자 정보를 수정합니다.")
    public String updateUser(
            @ToolParam(description = "수정할 사용자의 고유 ID", required = true) 
            long userId, 
            @ToolParam(description = "새로운 사용자 이름 (선택사항)", required = false) 
            String name, 
            @ToolParam(description = "새로운 이메일 주소 (선택사항)", required = false) 
            String email, 
            @ToolParam(description = "새로운 지역 ID (선택사항)", required = false) 
            Long regionId) {
        log.info("MCP Tool 호출: updateUser, userId={}, name={}, email={}, regionId={}",
                userId, name, email, regionId);

        try {
            // 사용자 존재 확인
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isEmpty()) {
                return "ID " + userId + "인 사용자를 찾을 수 없습니다.";
            }

            User updateUser = new User();
            updateUser.setName(name != null ? name.trim() : userOpt.get().getName());
            updateUser.setEmail(email != null ? email.trim() : userOpt.get().getEmail());

            // 지역 정보 설정
            if (regionId != null) {
                Optional<Region> regionOpt = regionService.findById(regionId);
                if (regionOpt.isEmpty()) {
                    return "ID " + regionId + "인 지역을 찾을 수 없습니다.";
                }
                updateUser.setRegion(regionOpt.get());
            } else {
                updateUser.setRegion(userOpt.get().getRegion());
            }

            Optional<User> updatedOpt = userService.update(userId, updateUser);
            if (updatedOpt.isEmpty()) {
                return "사용자 수정에 실패했습니다.";
            }

            User updated = updatedOpt.get();
            return String.format("""
                사용자가 성공적으로 수정되었습니다:
                - ID: %d
                - 이름: %s
                - 이메일: %s
                - 지역: %s
                """,
                    updated.getId(), updated.getName(), updated.getEmail(), updated.getRegion().getName());

        } catch (IllegalArgumentException e) {
            log.warn("사용자 수정 실패: {}", e.getMessage());
            return "사용자 수정 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("사용자 수정 중 오류", e);
            return "사용자 수정 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "사용자를 삭제합니다.")
    public String deleteUser(
            @ToolParam(description = "삭제할 사용자의 고유 ID", required = true) 
            long userId) {
        log.info("MCP Tool 호출: deleteUser, userId={}", userId);

        try {
            // 사용자 존재 확인
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isEmpty()) {
                return "ID " + userId + "인 사용자를 찾을 수 없습니다.";
            }

            boolean deleted = userService.delete(userId);
            if (deleted) {
                return "사용자가 성공적으로 삭제되었습니다: " + userOpt.get().getName();
            } else {
                return "사용자 삭제에 실패했습니다.";
            }

        } catch (Exception e) {
            log.error("사용자 삭제 중 오류", e);
            return "사용자 삭제 중 오류 발생: " + e.getMessage();
        }
    }
}
