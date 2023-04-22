package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Async_
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理 φ(゜▽゜*)♪")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询 🍥")
    public Result selectPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.selectPage(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐 eo(*≧▽≦)ツ┏━┓")
    public Result insert(@RequestBody SetmealDTO setmealDTO) {
        setmealService.insert(setmealDTO);

        return Result.success();
    }

    /**
     * 根据 id 查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询套餐 (●'◡'●)")
    public Result selectById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.selectById(id);

        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐 (^///^)")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        // TODO: 2023/4/22 前端传回的 idType 字段
        setmealService.update(setmealDTO);

        return Result.success();
    }

    /**
     * 启售、停售套餐
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启售、停售套餐 (p≧w≦q)")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        // TODO: 2023/4/22 套餐内包含未启售菜品
        setmealService.startOrStop(status, id);

        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐 {{{(>_<)}}}")
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.delete(ids);

        return Result.success();
    }
}
