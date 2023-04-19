package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类管理
 *
 * @author Async_
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
@Api(tags = "CategoryController ψ(｀∇´)ψ")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询 ( •̀ ω •́ )✧")
    public Result selectPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.selectPage(categoryPageQueryDTO);

        return Result.success(pageResult);
    }
}
