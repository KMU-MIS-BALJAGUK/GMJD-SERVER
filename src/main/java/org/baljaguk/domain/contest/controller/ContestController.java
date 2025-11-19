package org.baljaguk.domain.contest.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;
import org.baljaguk.domain.contest.service.ContestService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ContestListResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(contestService.search(keyword)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ContestListResponse>> getContestsWithFilterAndSort(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "latest") String sortType
    ) {
        ContestListResponse result = contestService.getContestsWithFilterAndSort(categoryId, sortType);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
