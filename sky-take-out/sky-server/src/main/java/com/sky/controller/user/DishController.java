package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C 端菜品管理
 *
 * @author Async_
 */
@Slf4j
@RequestMapping("/user/dish")
@Api(tags = "C 端菜品管理 φ(*￣0￣)")
@RestController("userDishController")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 根据 categoryId 查询所有菜品(dish 表中)
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据 categoryId 查询所有菜品 `(*>﹏<*)′")
    public Result selectAllByCategoryId(Long categoryId) {
        List<DishVO> dishVOList = dishService.selectAllByCategoryId(categoryId);

        return Result.success(dishVOList);
    }
}
