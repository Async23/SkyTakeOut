package com.sky.controller.user;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Async_
 */
@Api(tags = "C 端套餐管理 φ(゜▽゜*)♪")
@RequestMapping("/user/setmeal")
@RestController("userSetmealController")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类 id 查询套餐列表
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询套餐列表 (≧∇≦)ﾉ")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<SetmealVO>> selectByCategoryId(Long categoryId) {
        if (categoryId == null) {
            // 套餐查询参数有误
            throw new BaseException(MessageConstant.SETMEAL_QUERY_ILLEGAL_ARGUMENT);
        }

        List<SetmealVO> setmealVOList = setmealService.selectByCondition(Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build());

        return Result.success(setmealVOList);
    }

    /**
     * 根据套餐 id 查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐 id 查询菜品 (●'◡'●)")
    public Result<List<DishItemVO>> selectDishesById(@PathVariable Long id) {
        List<DishItemVO> dishItemVOList = setmealService.selectDishesById(id);

        return Result.success(dishItemVOList);
    }
}
