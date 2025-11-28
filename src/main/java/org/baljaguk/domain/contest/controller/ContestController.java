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

    @PostMapping
    @Operation(
            summary = "공모전 목록 조회 (검색 + 필터링 + 정렬 통합)",
            description = """
                하나의 API에서 검색어 기반 검색, 카테고리 필터링, 정렬, 페이지네이션을 모두 지원합니다.
                
                검색: SearchRequest (본문)
                필터링: categoryIdList
                정렬:
                   - latest   : 최신순
                   - popular  : 인기순
                   - deadline : 마감임박순
                페이지네이션: page, size
                """
    )
    public ResponseEntity<ApiResponse<ContestListResponse>> getContestList(
            @Valid @RequestBody SearchRequest keywordRequest,
            @RequestParam(required = false) List<Long> categoryIdList,
            @RequestParam(defaultValue = "latest") String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {

        ContestListResponse result = contestService.getContestList(
                keywordRequest,
                categoryIdList,
                sortType,
                page,
                size
        );

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
