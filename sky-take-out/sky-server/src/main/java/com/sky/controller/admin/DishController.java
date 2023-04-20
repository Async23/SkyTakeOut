package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜品管理
 *
 * @author Async_
 */
@Slf4j
@RestController
@Api(tags = "菜品管理 （＾∀＾●）ﾉｼ")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO
     * @return Result
     */
    @GetMapping("/page")
    @ApiOperation("分页查询 (○｀ 3′○)")
    public Result selectPage(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.selectPage(dishPageQueryDTO);

        return Result.success(pageResult);
    }
}
