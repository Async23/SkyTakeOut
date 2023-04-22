package com.sky.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.exception.BaseException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        if (setmealPageQueryDTO == null) {
            throw new BaseException(MessageConstant.SETMEAL_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }
        /*
            int page
            int pageSize
            String name
            Integer categoryId
            Integer status
         */
        String trimName = setmealPageQueryDTO.getName() == null ? null : setmealPageQueryDTO.getName().trim();
        boolean nameInvalid = trimName == null || "".equals(trimName);
        boolean categoryIdInvalid = setmealPageQueryDTO.getCategoryId() == null;
        boolean statusInvalid = setmealPageQueryDTO.getStatus() == null;

        // 任一有效，条件查询，忽略分页参数
        if (!nameInvalid || !categoryIdInvalid || !statusInvalid) {
            List<SetmealVO> setmealVOList = setmealMapper.selectByCondition(setmealPageQueryDTO);
            return new PageResult(setmealVOList.size(), setmealVOList);
        }

        // 所有参数均无效
        if (setmealPageQueryDTO.getPage() == null || setmealPageQueryDTO.getPageSize() == null) {
            throw new BaseException(MessageConstant.SETMEAL_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // 常规分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage = (Page<SetmealVO>) setmealMapper.selectAll();
        return new PageResult(setmealVOPage.getTotal(), setmealVOPage.getResult());
    }
}
