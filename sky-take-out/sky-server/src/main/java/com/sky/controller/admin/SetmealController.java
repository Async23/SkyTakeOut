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
}
