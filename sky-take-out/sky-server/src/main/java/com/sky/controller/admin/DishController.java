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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 管理端菜品管理
 *
 * @author Async_
 */
@Slf4j
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "管理端菜品管理 （＾∀＾●）ﾉｼ")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据 categoryId 查询所有菜品(dish 表中)，新增套餐时使用
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

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品 ^_~")
    public Result insert(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.insert(dishDTO);

        // 清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

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

        // 清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

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
    // TODO: 2023/4/21 菜品停售 => 关联套餐停售(已完成)
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

    /**
     * 清理 redis 缓存
     *
     * @param key
     */
    private void cleanCache(String key) {
        Set keys = redisTemplate.keys(key);
        log.info("清除 redis 缓存 keys:{}", keys);
        Long delete = redisTemplate.delete(keys);
        log.info("delete:{}", delete);
    }
}
