package com.example.cinemabooking.modules.movie.service;

import com.example.cinemabooking.modules.movie.dto.CategoryRequest;
import com.example.cinemabooking.modules.movie.dto.CategoryResponse;

import java.util.List;

public interface ICategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
