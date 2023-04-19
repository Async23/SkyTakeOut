package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.aspectj.weaver.BCException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        // 分类名称与分类类型均为空，分页参数为空
        if (categoryPageQueryDTO.getPage() == null || categoryPageQueryDTO.getPageSize() == null) {
            throw new BaseException(MessageConstant.CATEGORY_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // 分类名称与分类类型均为空，分页参数不为空，常规分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> categoryPage = (Page<Category>) categoryMapper.selectAll();
        return new PageResult(categoryPage.getTotal(), categoryPage.getResult());
    }

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public void insert(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new BaseException(MessageConstant.CATEGORY_ILLEGAL_ARGUMENT);
        }

        if (categoryDTO.getName() == null || categoryDTO.getSort() == null || categoryDTO.getType() == null) {
            throw new BaseException(MessageConstant.CATEGORY_ILLEGAL_ARGUMENT);
        }

        Category category = Category.builder()
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createUser(BaseContext.getCurrentId())
                .updateUser(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.insert(category);
    }
}
