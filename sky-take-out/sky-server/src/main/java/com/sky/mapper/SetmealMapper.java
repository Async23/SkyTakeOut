package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Mapper
public interface SetmealMapper {
    List<SetmealVO> selectByCondition(SetmealPageQueryDTO setmealPageQueryDTO);

    List<SetmealVO> selectAll();

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    void insertSetMealDish(List<SetmealDish> setmealDishList);

    SetmealVO selectById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    void updateSetMealDish(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);

    List<Map<String, Object>> selectStatusByIds(List<Long> ids);

    void deleteByIds(List<Long> validIds);

    Integer selectStopDishCounts(Long id);

    List<SetmealVO> selectByCategoryId(Long categoryId);
}
