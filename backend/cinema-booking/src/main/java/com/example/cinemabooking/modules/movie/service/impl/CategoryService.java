package com.example.cinemabooking.modules.movie.service.impl;

import com.example.cinemabooking.common.exception.BadRequestException;
import com.example.cinemabooking.common.exception.DuplicateResourceException;
import com.example.cinemabooking.common.exception.ResourceNotFoundException;
import com.example.cinemabooking.modules.movie.dto.CategoryRequest;
import com.example.cinemabooking.modules.movie.dto.CategoryResponse;
import com.example.cinemabooking.modules.movie.entity.Category;
import com.example.cinemabooking.modules.movie.repository.ICategoryRepository;
import com.example.cinemabooking.modules.movie.repository.IMovieCategoryRepository;
import com.example.cinemabooking.modules.movie.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IMovieCategoryRepository movieCategoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name")).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(findCategory(id));
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String name = request.getName().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Thể loại '" + name + "' đã tồn tại");
        }
        Category category = new Category();
        category.setName(name);
        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        String name = request.getName().trim();
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Thể loại '" + name + "' đã tồn tại");
        }
        category.setName(name);
        return toResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategory(id);
        if (movieCategoryRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Không thể xóa thể loại đang được gán cho phim");
        }
        categoryRepository.delete(category);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thể loại", id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
