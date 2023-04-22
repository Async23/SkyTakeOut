package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
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

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @Override
    public void insert(SetmealDTO setmealDTO) {
        if (setmealDTO == null) {
            // 新增套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_INSERT_ILLEGAL_ARGUMENT);
        }

        String trimName = setmealDTO.getName() == null ? null : setmealDTO.getName().trim();
        boolean nameInvalid = trimName == null || "".equals(trimName);
        boolean priceInvalid = setmealDTO.getPrice() == null;
        boolean categoryIdInvalid = setmealDTO.getCategoryId() == null;
        if (nameInvalid || priceInvalid || categoryIdInvalid) {
            // 新增套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_INSERT_ILLEGAL_ARGUMENT);
        }

        setmealDTO.setName(trimName);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // Mapper 层：插入 setmeal 表
        setmealMapper.insert(setmeal);

        // Mapper 层：插入 setmeal_dish 表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealMapper.insertSetMealDish(setmealDishes);

    }

    /**
     * 根据 id 查询套餐
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {
        if (id == null) {
            // 套餐查询参数有误
            throw new BaseException(MessageConstant.SETMEAL_QUERY_ILLEGAL_ARGUMENT);
        }

        return setmealMapper.selectById(id);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        if (setmealDTO == null) {
            // 修改套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_UPDATE_ILLEGAL_ARGUMENT);
        }

        String trimName = setmealDTO.getName() == null ? null : setmealDTO.getName().trim();
        boolean nameInvalid = trimName == null || "".equals(trimName);
        boolean priceInvalid = setmealDTO.getPrice() == null;
        boolean categoryIdInvalid = setmealDTO.getCategoryId() == null;
        if (nameInvalid || priceInvalid || categoryIdInvalid) {
            // 修改套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_UPDATE_ILLEGAL_ARGUMENT);
        }

        setmealDTO.setName(trimName);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // Mapper 层：修改 setmeal 表
        setmealMapper.update(setmeal);

        // Mapper 层：修改 setmeal_dish 表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealMapper.updateSetMealDish(setmealDTO);

    }
}
