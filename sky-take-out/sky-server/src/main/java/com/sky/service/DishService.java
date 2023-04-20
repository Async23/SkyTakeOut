package com.sky.service;

import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

/**
 * @author Async_
 */
public interface DishService {
    PageResult selectPage(DishPageQueryDTO dishPageQueryDTO);

    DishVO selectById(Long id);
}
