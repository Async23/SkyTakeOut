package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author Async_
 */
public interface DishService {
    PageResult selectPage(DishPageQueryDTO dishPageQueryDTO);

    DishVO selectById(Long id);

    void insert(DishDTO dishDTO);

    void update(DishDTO dishDTO);

    void startOrStop(Integer status, Integer id);

    void delete(List<Long> ids);
}
