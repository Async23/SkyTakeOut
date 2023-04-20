package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO);

    void insert(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void startOrStop(Integer status, Long id);

    void logicDelete(Long id);

    List<Category> selectAllByType(Integer type);
}
