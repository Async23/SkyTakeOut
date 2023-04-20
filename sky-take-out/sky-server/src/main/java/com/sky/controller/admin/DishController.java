package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 根据 id 查询（修改按钮）
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询 (/▽＼)")
    public Result selectById(@PathVariable Long id) {
        DishVO dishVO = dishService.selectById(id);

        return Result.success(dishVO);
    }

    // @PostMapping
    // @ApiOperation("新增菜品 ^_~")
    // public Result
}
