package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.Orders;

import java.util.List;

/**
 * @author Async_
 */
public interface DishService {
    PageResult selectPage(DishPageQueryDTO dishPageQueryDTO);

    Orders selectById(Long id);

    void insert(DishDTO dishDTO);

    void update(DishDTO dishDTO);

    void startOrStop(Integer status, Integer id);

    void delete(List<Long> ids);

    List<DishVO> selectAllByCategoryId(Long categoryId);

    List<DishVO> listWithFlavors(Dish queryDish);
}
