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
        boolean nameInvalid = trimName == null || "".equals(trimName);
        boolean typeInvalid = categoryPageQueryDTO.getType() == null;
        if (!(nameInvalid && typeInvalid)) {
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
            throw new BaseException(MessageConstant.CATEGORY_INSERT_ILLEGAL_ARGUMENT);
        }

        if (categoryDTO.getName() == null || categoryDTO.getSort() == null || categoryDTO.getType() == null) {
            throw new BaseException(MessageConstant.CATEGORY_INSERT_ILLEGAL_ARGUMENT);
        }

        Category category = Category.builder()
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createUser(BaseContext.getCurrentId())
                .updateUser(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(categoryDTO, category);

        // 判断新增套餐名是否与逻辑删除套餐重复
        Integer isDelete = categoryMapper.selectIsDeleteByName(categoryDTO.getName());
        // 表中不存在 => 直接新增；表中已存在（未被逻辑删除） => 主动冲突
        if (isDelete == null || isDelete == 0) {
            categoryMapper.insert(category);
        } else {
            // 1(被逻辑删除)
            categoryMapper.updateByName(category);
        }

    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            throw new BaseException(MessageConstant.CATEGORY_UPDATE_ILLEGAL_ARGUMENT);
        }

        if (categoryDTO.getName() == null || categoryDTO.getSort() == null || categoryDTO.getId() == null) {
            throw new BaseException(MessageConstant.CATEGORY_UPDATE_ILLEGAL_ARGUMENT);
        }

        categoryMapper.updateById(categoryDTO);
    }

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        boolean statusInvalid = status == null || (status != 0 && status != 1);
        boolean idInvalid = id == null;
        if (statusInvalid || idInvalid) {
            throw new BaseException(MessageConstant.CATEGORY_START_OR_STOP_ILLEGAL_ARGUMENT);
        }

        categoryMapper.startOrStop(status, id);
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Override
    public void logicDelete(Long id) {
        if (id == null) {
            throw new BaseException(MessageConstant.CATEGORY_DELETE_ILLEGAL_ARGUMENT);
        }

        Integer counts = categoryMapper.selectRelatedDishesCounts(id);
        if (!(counts.equals(0))) {
            throw new BaseException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        categoryMapper.logicDelete(id);
    }

    /**
     * 根据 type 查询所有 category
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> selectAllByType(Integer type) {
        if (type == null) {
            // 与 admin 中区别
            return categoryMapper.selectAll();
        }

        return categoryMapper.selectAllByType(type);
    }
}
