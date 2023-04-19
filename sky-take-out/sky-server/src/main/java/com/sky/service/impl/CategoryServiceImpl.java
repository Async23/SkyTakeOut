package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        if (categoryPageQueryDTO == null) {
            throw new BaseException(MessageConstant.CATEGORY_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // 分类名称 与 分类类型 任一有效，则忽略分页参数，根据相应条件查询
        String trimName = categoryPageQueryDTO.getName() == null ? null : categoryPageQueryDTO.getName().trim();
        boolean nameNotValid = trimName == null || "".equals(trimName);
        boolean typeNotValid = categoryPageQueryDTO.getType() == null;
        if (!(nameNotValid && typeNotValid)) {
            List<Category> categoryList = categoryMapper.selectByNameOrType(trimName, categoryPageQueryDTO.getType());
            return new PageResult(categoryList.size(), categoryList);
        }

        // 分类名称 与 分类类型 无效，分页参数无效
        if (categoryPageQueryDTO.getPage() == null || categoryPageQueryDTO.getPageSize() == null) {
            throw new BaseException(MessageConstant.CATEGORY_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // 分类名称 与 分类类型 无效，分页参数有效，常规分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> categoryPage = (Page<Category>) categoryMapper.selectAll();
        return new PageResult(categoryPage.getTotal(), categoryPage.getResult());
    }
}
