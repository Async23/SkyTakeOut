package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Async
 */
@Slf4j
@RequestMapping("/user/category")
@RestController("userCategoryController")
@Api(tags = "C 段分类模块 CategoryController ╰(*°▽°*)╯")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据 type 查询所有 category<p>
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据 type 查询 category (≧﹏ ≦)")
    public Result selectAllByType(Integer type) {
        List<Category> categoryList = categoryService.selectAllByType(type);

        return Result.success(categoryList);
    }
}
