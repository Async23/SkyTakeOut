package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Result selectByCategoryId(Long categoryId) {
        List<SetmealVO> setmealVOList = setmealService.selectByCategoryId(categoryId);

        return Result.success(setmealVOList);
    }
}
