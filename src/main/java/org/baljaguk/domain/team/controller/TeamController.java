package org.baljaguk.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.dto.response.TeamDetailResponse;
import org.baljaguk.domain.team.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 팀 관련 HTTP 요청을 받는 컨트롤러 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    /** 팀 상세 조회 API */
    @GetMapping("/{teamId}")
    public TeamDetailResponse getTeamDetail(@PathVariable Long teamId) {
        // 서비스 계층에 팀 상세 조회를 위임하고 그 결과를 그대로 응답으로 반환
        return teamService.getTeamDetail(teamId);
    }
}
