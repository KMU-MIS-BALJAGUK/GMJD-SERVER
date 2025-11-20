package org.baljaguk.domain.contest.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.contest.dto.request.SearchRequest;
import org.baljaguk.domain.contest.dto.response.ContestDetailResponse;
import org.baljaguk.domain.contest.dto.response.ContestListResponse;
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
    @Operation(summary = "공모전 상세 페이지 조회",
            description = "공모전 id로 상세 페이지를 조회합니다.")
    public ResponseEntity<ApiResponse<ContestDetailResponse>> getContestDetail(@PathVariable Long contestId) {
        ContestDetailResponse detailResponse = contestService.getContestDetail(contestId);
        return ResponseEntity.ok(ApiResponse.ok(detailResponse));
    }

    @PostMapping("/search")
    @Operation(summary = "검색어로 공모전 조회",
            description = "공모전 제목, 공모전 주최기업명, 기업형태 중 검색어가 포함된 공모전을 조회합니다.")
    public ResponseEntity<ApiResponse<ContestListResponse>> search(@Valid @RequestBody SearchRequest keyword) {
        return ResponseEntity.ok(ApiResponse.ok(contestService.search(keyword)));
    }

    @GetMapping
    @Operation(summary = "필터링 및 정렬된 공모전 조회",
            description = "필터링 및 정렬하여 공모전을 조회합니다.\n" +
                    "\n필터링은 필수가 아닙니다." +
                    "\n필터링은 카테고리 id 리스트로 입력합니다." +
                    "\n정렬은" +
                    "\n  - 전체(최신순) : latest" +
                    "\n  - 인기순 : popular" +
                    "\n  - 마감임박순 : deadline")
    public ResponseEntity<ApiResponse<ContestListResponse>> getContestsWithFilterAndSort(
            @RequestParam(required = false) List<Long> categoryIdList,
            @RequestParam(defaultValue = "latest") String sortType
    ) {
        ContestListResponse result = contestService.getContestsWithFilterAndSort(categoryIdList, sortType);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
