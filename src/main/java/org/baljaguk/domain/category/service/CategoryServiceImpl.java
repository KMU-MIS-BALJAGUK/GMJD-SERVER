package org.baljaguk.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.category.dto.response.CategoriesResponse;
import org.baljaguk.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoriesResponse getAllCategories() {
        List<CategoriesResponse.CategoryResponse> categoryResponses = categoryRepository.findAll().stream()
                .map(CategoriesResponse.CategoryResponse::from)
                .toList();

        return CategoriesResponse.from(categoryResponses);
    }
}
