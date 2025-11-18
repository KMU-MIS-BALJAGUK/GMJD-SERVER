package org.baljaguk.domain.category.controller;

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

    @GetMapping
    public ResponseEntity<ApiResponse<CategoriesResponse>> getAllCategories() {
        CategoriesResponse categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }
}
