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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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

        boolean nameInvalid = dishPageQueryDTO.getName() == null || "".equals(dishPageQueryDTO.getName().trim());
        String trimName = dishPageQueryDTO.getName() == null ? null : dishPageQueryDTO.getName().trim();
        boolean categoryInvalid = dishPageQueryDTO.getCategoryId() == null;
        boolean statusInvalid = dishPageQueryDTO.getStatus() == null;

        // 名称、分类、状态 任一有效，则忽略分页参数，根据相应条件查询
        if (!(nameInvalid && categoryInvalid && statusInvalid)) {
            List<DishVO> dishList = dishMapper.selectByNameOrCategoryIdOrStatus(trimName, dishPageQueryDTO.getCategoryId(), dishPageQueryDTO.getStatus());
            return new PageResult(dishList.size(), dishList);
        }

        // 名称、分类、状态 均无效，分页参数无效
        if (dishPageQueryDTO.getPage() == null || dishPageQueryDTO.getPageSize() == null) {
            throw new BaseException(MessageConstant.DISH_QUERY_PAGE_ILLEGAL_ARGUMENT);
        }

        // ------------------------------------------------------------------------
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
    @Transactional
    public void update(DishDTO dishDTO) {
        if (dishDTO == null) {
            throw new BaseException(MessageConstant.DISH_UPDATE_ILLEGAL_ARGUMENT);
        }

        // 包装为 Dish 类型
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        String trimName = dish.getName() == null ? null : dish.getName().trim();
        if (trimName == null || "".equals(trimName)) {
            throw new BaseException(MessageConstant.DISH_UPDATE_ILLEGAL_ARGUMENT);
        }

        String trimImage = dish.getImage() == null ? null : dish.getImage().trim();
        String trimDescription = dish.getDescription() == null ? null : dish.getDescription().trim();
        dish.setName(trimName);
        dish.setImage(trimImage);
        dish.setDescription(trimDescription);

        // Mapper 层(更新 dish 表)
        dishMapper.updateDish(dish);

        // 方法一
        /*// Mapper 层(删除 dish_flavor 表)
        dishMapper.deleteDishFlavor(dish.getId());
        // Mapper 层(插入 dish_flavor 表)
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        dishMapper.insertDishFlavor(flavors);*/

        // 方法二
        // Mapper 层(删除 dish_flavor 表 => 插入 dish_flavor 表)
        dishDTO.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
        dishMapper.updateDishFlavor(dishDTO);
    }

    /**
     * 启售、停售菜品
     *
     * @param status
     * @return
     */
    @Override
    public void startOrStop(Integer status, Integer id) {
        boolean statusInvalid = status == null || !(status == 0 || status == 1);
        if (statusInvalid || id == null) {
            throw new BaseException(MessageConstant.DISH_START_OR_STOP_ILLEGAL_ARGUMENT);
        }

        dishMapper.startOrStop(status, id);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @Override
    public void delete(List<Long> ids) {
        if (ids == null) {
            throw new BaseException(MessageConstant.DISH_DELETE_ILLEGAL_ARGUMENT);
        }

        if (ids.size() == 0) {
            return;
        }

        List<Map<String, Object>> results = dishMapper.selectStatusAndRelatedCountsByIds(ids);

        // TODO: 2023/4/21 优化，直接传入 results 而非 deleteIds
        List<Long> deleteIds = new ArrayList<>();
        results.forEach(result -> {
            if (result.get("relatedCounts").equals(0L) && result.get("status").equals(0)) {
                deleteIds.add((Long) result.get("id"));
            }
        });
        // Mapper 层
        dishMapper.deleteByIds(deleteIds);

        StringBuilder msg = new StringBuilder();
        for (Map<String, Object> result : results) {
            String str = "";
            boolean onSale = result.get("status").equals(1);
            boolean isRelated = !result.get("relatedCounts").equals(0L);
            if (onSale || isRelated) {
                str += "无法删除" + result.get("name") + "(";
            }
            if (onSale) {
                str += "为启售状态";
            }
            if (isRelated) {
                str += (onSale ? "且" : "") + "有套餐关联";
            }
            if (onSale || isRelated) {
                str += ")";
            }
            msg.append(str);
        }
        if (!("".equals(msg.toString()))) {
            throw new BaseException(msg.toString());
        }
    }

    /**
     * 根据 categoryId 查询所有菜品(dish 表中)
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> selectAllByCategoryId(Long categoryId) {
        if (categoryId == null) {
            // 菜品查询参数有误
            throw new BaseException(MessageConstant.DISH_QUERY_ILLEGAL_ARGUMENT);
        }

        return listWithFlavors(Dish.builder().categoryId(categoryId).build());
    }

    @Override
    public List<DishVO> listWithFlavors(Dish queryDish) {
        return dishMapper.listWithFlavors(queryDish);
    }

}
