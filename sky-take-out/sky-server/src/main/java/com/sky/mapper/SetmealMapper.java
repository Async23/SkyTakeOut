package com.sky.mapper;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Mapper
public interface SetmealMapper {
    List<SetmealVO> selectByCondition(SetmealPageQueryDTO setmealPageQueryDTO);

    List<SetmealVO> selectAll();
}
