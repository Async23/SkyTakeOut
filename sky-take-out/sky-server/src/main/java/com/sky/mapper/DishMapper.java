package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品管理
 *
 * @author Async_
 */
@Mapper
public interface DishMapper {
    List<Dish> selectByNameOrCategoryIdOrStatus(String name, Integer categoryId, Integer status);

    List<Dish> selectAll();
}
