package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
