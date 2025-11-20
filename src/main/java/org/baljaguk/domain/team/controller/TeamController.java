package org.baljaguk.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.team.service.TeamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeamController {

    private final TeamService teamService;

}
