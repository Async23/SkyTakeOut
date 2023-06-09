package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 套餐管理
 *
 * @author Async_
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

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
        setmealPageQueryDTO.setName(trimName);
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

        // 准备数据
        setmealDTO.setName(trimName);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
        // 逻辑补充：更改套餐时，如果菜品中有停售菜品，该套餐也需要变为停售
        List<Long> dishIds = new ArrayList<>();
        setmealDishes.forEach(setmealDish -> dishIds.add(setmealDish.getDishId()));
        List<Map<String, Object>> results = dishMapper.selectStatusAndRelatedCountsByIds(dishIds);
        for (Map<String, Object> result : results) {
            if (result.get("status").equals(StatusConstant.DISABLE)) {
                setmealDTO.setStatus(0);
                break;
            }
        }
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // Mapper 层：修改 setmeal_dish 表
        setmealMapper.updateSetMealDish(setmealDTO);

        // Mapper 层：修改 setmeal 表
        setmealMapper.update(setmeal);

    }

    /**
     * 启售、停售套餐
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        boolean statusInvalid = status == null || !(status == 1L || status == 0L);
        boolean idInvalid = id == null;
        if (statusInvalid || idInvalid) {
            // 启售、停售套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_START_OR_STOP_ILLEGAL_ARGUMENT);
        }

        // Mapper 层：查询包含未启售菜品的数量
        Integer dishStatus = setmealMapper.selectStopDishCounts(id);
        if (dishStatus > 0 && status == 1) {
            // 套餐内包含未启售菜品，无法启售
            throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }

        // Mapper 层：启用、停用
        setmealMapper.startOrStop(status, id);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @Override
    public void delete(List<Long> ids) {
        if (ids == null) {
            // 删除套餐参数有误
            throw new BaseException(MessageConstant.SETMEAL_DELETE_ILLEGAL_ARGUMENT);
        }

        if (ids.size() == 0) {
            return;
        }

        // Mapper 层，查询状态
        List<Map<String, Object>> setmealStatus = setmealMapper.selectStatusByIds(ids);
        List<Long> validIds = new ArrayList<>();
        StringBuilder msg = new StringBuilder();
        for (Map<String, Object> setmeal : setmealStatus) {
            if (setmeal.get("status").equals(StatusConstant.DISABLE)) {
                // 停售状态，将被删除
                validIds.add((Long) setmeal.get("id"));
            } else {
                // 启售状态，加入信息
                msg.append(setmeal.get("name")).append("无法删除(为启售状态)");
            }
        }

        // Mapper 层，批量删除
        if (validIds.size() > 0) {
            setmealMapper.deleteByIds(validIds);
        }

        if (!"".equals(msg.toString())) {
            // 部分启售中的套餐不能删除
            throw new BaseException(msg.toString());
        }
    }

    /**
     * 根据分类 id 查询套餐列表
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<SetmealVO> selectByCategoryId(Long categoryId) {
        if (categoryId == null) {
            // 套餐查询参数有误
            throw new BaseException(MessageConstant.SETMEAL_QUERY_ILLEGAL_ARGUMENT);
        }

        return setmealMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据套餐 id 查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> selectDishesById(Long id) {
        if (id == null) {
            // 菜品查询参数有误
            throw new BaseException(MessageConstant.DISH_QUERY_ILLEGAL_ARGUMENT);
        }

        return setmealMapper.selectDishesById(id);
    }

    /**
     * 根据条件查询
     *
     * @param setmeal
     * @return
     */
    @Override
    public List<SetmealVO> selectByCondition(Setmeal setmeal) {
        if (setmeal == null) {
            // 套餐查询参数有误
            throw new BaseException(MessageConstant.SETMEAL_QUERY_ILLEGAL_ARGUMENT);
        }
        return setmealMapper.listWithDishes(setmeal);
    }
}
