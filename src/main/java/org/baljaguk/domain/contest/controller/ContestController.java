package org.baljaguk.domain.contest.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.service.ContestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;
}
