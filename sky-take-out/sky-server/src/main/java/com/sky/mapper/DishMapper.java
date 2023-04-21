package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

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

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    void insertDishFlavor(List<DishFlavor> dishFlavors);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    void deleteDishFlavor(Long dishId);

    void updateDishFlavor(DishDTO dishDTO);

    void startOrStop(@Param("status") Integer status, @Param("id") Integer id);
}
