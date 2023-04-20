package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品管理
 *
 * @author Async_
 */
@Mapper
public interface DishMapper {
    List<DishVO> selectByNameOrCategoryIdOrStatus(String name, Integer categoryId, Integer status);

    List<DishVO> selectAll();

    DishVO selectById(Long id);

    List<DishFlavor> selectFlavors(Long flavorId);
}
