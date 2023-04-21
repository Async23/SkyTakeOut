package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
            List<DishVO> dishList = dishMapper.selectByNameOrCategoryIdOrStatus(trimName, dishPageQueryDTO.getCategoryId(), dishPageQueryDTO.getStatus());
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

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    @Override
    public DishVO selectById(Long id) {
        if (id == null) {
            throw new BaseException(MessageConstant.DISH_QUERY_ILLEGAL_ARGUMENT);
        }

        return dishMapper.selectById(id);
    }

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @Override
    public void insert(DishDTO dishDTO) {
        if (dishDTO == null) {
            throw new BaseException(MessageConstant.DISH_INSERT_ILLEGAL_ARGUMENT);
        }

        // 包装为 Dish 类型
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // Mapper 层
        dishMapper.insertDish(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        // Mapper 层
        dishMapper.insertDishFlavor(flavors);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @Override
    public void update(DishDTO dishDTO) {
        if (dishDTO == null) {
            throw new BaseException(MessageConstant.DISH_UPDATE_ILLEGAL_ARGUMENT);
        }

        // 包装为 Dish 类型
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        String trimName = dish.getName() == null ? null : dish.getName().trim();
        String trimImage = dish.getImage() == null ? null : dish.getImage().trim();
        String trimDescription = dish.getDescription() == null ? null : dish.getDescription().trim();
        dish.setName(trimName);
        dish.setImage(trimImage);
        dish.setDescription(trimDescription);

        // Mapper 层(更新 dish 表)
        dishMapper.updateDish(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        // TODO: 2023/4/21
        // Mapper 层(删除 dish_flavor 表)
        dishMapper.deleteDishFlavor(dish.getId());
        // Mapper 层(插入 dish_flavor 表)
        dishMapper.insertDishFlavor(flavors);
    }
}
