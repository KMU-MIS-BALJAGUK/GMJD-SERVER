package org.baljaguk.domain.category.service;

import org.baljaguk.domain.category.dto.response.CategoriesResponse;
import org.baljaguk.domain.category.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoriesResponse getAllCategories();
}
