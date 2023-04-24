package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Async
 */
@Slf4j
@RestController("userShopController")
@Api(tags = "营业状态 ShopController (/≧▽≦)/")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户端查询营业状态
     *
     * @return
     */
    @GetMapping("/user/shop/status")
    @ApiOperation("用户端查询营业状态 (⓿_⓿)")
    public Result userSelectShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("status");

        return Result.success(status);
    }
}
