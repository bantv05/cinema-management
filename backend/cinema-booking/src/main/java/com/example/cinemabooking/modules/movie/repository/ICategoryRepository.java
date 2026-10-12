package com.example.cinemabooking.modules.movie.repository;

import com.example.cinemabooking.modules.movie.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    /** Dung khi update: kiem tra trung ten voi the loai KHAC (bo qua chinh no). */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
