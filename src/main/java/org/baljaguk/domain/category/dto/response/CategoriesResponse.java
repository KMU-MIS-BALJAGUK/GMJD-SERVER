package org.baljaguk.domain.category.dto.response;

import java.util.List;

public record CategoriesResponse(List<CategoryResponse> categories) {

    public static CategoriesResponse from(List<CategoryResponse> categories) {
        return new CategoriesResponse(categories);
    }

    public record CategoryResponse(Long id, String name) {
        public static CategoryResponse from(org.baljaguk.domain.category.entity.Category category) {
            return new CategoryResponse(category.getId(), category.getName());
        }
    }
}
