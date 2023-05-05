package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import com.sky.vo.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜品管理
 *
 * @author Async_
 */
@Mapper
public interface DishMapper {
    List<Orders> selectByNameOrCategoryIdOrStatus(String name, Integer categoryId, Integer status);

    List<Orders> selectAll();

    Orders selectById(Long id);

    List<DishFlavor> selectFlavors(Long flavorId);

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    void insertDishFlavor(List<DishFlavor> dishFlavors);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    void deleteDishFlavor(Long dishId);

    void updateDishFlavor(DishDTO dishDTO);

    void startOrStop(@Param("status") Integer status, @Param("id") Integer id);

    void deleteByIds(List<Long> ids);

    List<Map<String, Object>> selectStatusAndRelatedCountsByIds(List<Long> ids);

    List<DishVO> listWithFlavors(Dish queryDish);

    Dish getById(Long dishId);

    /**
     * 根据 map 中的 status 获取菜品的数量
     * @param map
     * @return
     */
    Integer countByMap(Map<String,Integer> map);
}
