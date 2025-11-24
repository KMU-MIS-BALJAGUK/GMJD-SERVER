package org.baljaguk.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.category.dto.response.CategoriesResponse;
import org.baljaguk.domain.category.service.CategoryService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "전체 카테고리 조회",
            description = "공모전/팀 생성 시 사용되는 전체 카테고리 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<CategoriesResponse>> getAllCategories() {
        CategoriesResponse categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }
}
