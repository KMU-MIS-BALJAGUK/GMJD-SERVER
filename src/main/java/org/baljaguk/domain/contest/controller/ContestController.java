package org.baljaguk.domain.contest.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestSearchResponse;
import org.baljaguk.domain.contest.service.ContestService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<ContestDetailResponse>> getContestDetail(@PathVariable Long contestId) {
        ContestDetailResponse detailResponse = contestService.getContestDetail(contestId);
        return ResponseEntity.ok(ApiResponse.ok(detailResponse));
    }

    @GetMapping("/api/contests/search")
    public ResponseEntity<ApiResponse<List<ContestSearchResponse>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(contestService.search(keyword)));
    }
}
