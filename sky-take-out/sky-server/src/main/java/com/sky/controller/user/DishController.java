package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import com.sky.vo.Orders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C 端菜品管理
 *
 * @author Async_
 */
@Slf4j
@RequestMapping("/user/dish")
@Api(tags = "C 端菜品管理 φ(*￣0￣)")
@RestController("userDishController")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据 categoryId 查询所有菜品(dish 表中)
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据 categoryId 查询所有菜品 `(*>﹏<*)′")
    public Result<List<DishVO>> selectAllByCategoryId(Long categoryId) {
        String key = "dish_" + categoryId;
        log.info("根据 categoryId 查询所有菜品，key:{}", key);

        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            log.info("redis 缓存未命中 ×");
            List<DishVO> dishVOListMySQL = dishService.listWithFlavors(Dish.builder().categoryId(categoryId).build());
            redisTemplate.opsForValue().set(key, dishVOListMySQL);
            return Result.success(dishVOListMySQL);
        }

        log.info("redis 缓存命中 √");
        List<DishVO> dishVOList = (List<DishVO>) obj;
        return Result.success(dishVOList);
    }
}
