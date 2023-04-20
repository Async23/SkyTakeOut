package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 菜品管理
 *
 * @author Async_
 */
@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO
     * @return PageResult
     */
    @Override
    public PageResult selectPage(DishPageQueryDTO dishPageQueryDTO) {
        if (dishPageQueryDTO == null) {
            throw new BaseException(MessageConstant.DISH_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        boolean nameNotValid = dishPageQueryDTO.getName() == null || "".equals(dishPageQueryDTO.getName().trim());
        String trimName = dishPageQueryDTO.getName() == null ? null : dishPageQueryDTO.getName().trim();
        boolean categoryNotValid = dishPageQueryDTO.getCategoryId() == null;
        boolean statusNotValid = dishPageQueryDTO.getStatus() == null;

        // 名称、分类、状态 任一有效，则忽略分页参数，根据相应条件查询
        if (!(nameNotValid && categoryNotValid && statusNotValid)) {
            List<Dish> dishList = dishMapper.selectByNameOrCategoryIdOrStatus(trimName, dishPageQueryDTO.getCategoryId(), dishPageQueryDTO.getStatus());
            return new PageResult(dishList.size(), dishList);
        }

        // 名称、分类、状态 均无效，分页参数无效
        if (dishPageQueryDTO.getPage() == null || dishPageQueryDTO.getPageSize() == null) {
            throw new BaseException(MessageConstant.DISH_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // 名称、分类、状态 均无效，分页参数有效，执行正常分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = (Page<DishVO>) dishMapper.selectAll();
        return new PageResult(dishPage.getTotal(), dishPage.getResult());
    }
}
