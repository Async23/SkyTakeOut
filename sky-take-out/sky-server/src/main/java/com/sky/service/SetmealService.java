package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(SetmealDTO setmealDTO);

    SetmealVO selectById(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);

    void delete(List<Long> ids);

    List<SetmealVO> selectByCategoryId(Long categoryId);

    List<DishItemVO> selectDishesById(Long id);
}
