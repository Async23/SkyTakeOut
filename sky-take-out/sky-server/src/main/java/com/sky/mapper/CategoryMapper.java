package com.sky.mapper;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Mapper
public interface CategoryMapper {
    List<Category> selectByNameOrType(String name, Integer type);

    List<Category> selectAll();

    void insert(Category category);

    void update(CategoryDTO categoryDTO);

    void startOrStop(@Param("status") Integer status, @Param("id") Long id);
}
