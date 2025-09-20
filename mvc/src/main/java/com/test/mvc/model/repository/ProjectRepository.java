package com.test.mvc.model.repository;

import com.test.mvc.model.entity.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // ค้นหาตามชื่อ (case-insensitive) + เรียงด้วย Sort
    List<Project> findByNameContainingIgnoreCase(String name, Sort sort);

    // ค้นหาตามหมวดหมู่ + ชื่อ + เรียงด้วย Sort
    List<Project> findByCategory_IdAndNameContainingIgnoreCase(Long categoryId, String name, Sort sort);
}
