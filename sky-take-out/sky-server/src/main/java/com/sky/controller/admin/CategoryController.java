package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 *
 * @author Async_
 */
@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Api(tags = "分类管理 ψ(｀∇´)ψ")
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

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类 \\\\^o^/")
    public Result insert(@RequestBody CategoryDTO categoryDTO) {
        categoryService.insert(categoryDTO);

        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类 (￣_,￣ )")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类 ⊙﹏⊙∥")
    public Result logicDelete(Long id) {
        categoryService.logicDelete(id);

        return Result.success();
    }

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类 ＞︿＜")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);

        return Result.success();
    }

    /**
     * 根据 type 查询所有 category<p>
     * (点击 “套餐管理” 和 “新建套餐” 时会使用)
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据 type 查询 category (ಥ _ ಥ)")
    public Result selectAllByType(Integer type) {
        List<Category> categoryList = categoryService.selectAllByType(type);

        return Result.success(categoryList);
    }
}
