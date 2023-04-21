package com.sky.controller.admin;

import com.sky.dto.DishDTO;
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

import java.util.List;

/**
 * 菜品管理
 *
 * @author Async_
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理 （＾∀＾●）ﾉｼ")
// TODO: 2023/4/21 菜品停售 => 套餐停售
// TODO: 2023/4/21 菜品被套餐使用 => 菜品无法删除
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
        log.warn(Thread.currentThread().getName());
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

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品 ^_~")
    public Result insert(@RequestBody DishDTO dishDTO) {
        dishService.insert(dishDTO);

        return Result.success();
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品 ヽ（≧□≦）ノ")
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);

        return Result.success();
    }

    /**
     * 启售、停售菜品
     *
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启售、停售菜品 U_U")
    @PostMapping("/status/{status}")
    // TODO: 2023/4/21 菜品停售 => 关联套餐停售
    public Result startOrStop(@PathVariable Integer status, Integer id) {
        dishService.startOrStop(status, id);

        return Result.success();
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品 (╬▔皿▔)╯")
    public Result delete(@RequestParam("ids") List<Long> ids) {
        dishService.delete(ids);

        return Result.success();
    }
}
