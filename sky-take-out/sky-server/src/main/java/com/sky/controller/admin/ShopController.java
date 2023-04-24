package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Async
 */
@Slf4j
@RestController("adminShopController")
@Api(tags = "营业状态 ShopController (/≧▽≦)/")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 管理端修改营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/admin/shop/{status}")
    @ApiOperation("管理端修改营业状态 o(TヘTo)")
    public Result updateShopStatus(@PathVariable Integer status) {
        boolean statusInvalid = status == null || !(status.equals(StatusConstant.DISABLE) || status.equals(StatusConstant.ENABLE));
        if (statusInvalid) {
            // 修改店铺状态参数有误
            throw new BaseException(MessageConstant.UPDATE_SHOP_STATUS_ILLEGAL_ARGUMENT);
        }

        redisTemplate.opsForValue().set("status", status);
        return Result.success();
    }

    /**
     * 管理端查询营业状态
     *
     * @return
     */
    @GetMapping("/admin/shop/status")
    @ApiOperation("管理端查询营业状态 (⓿_⓿)")
    public Result adminSelectShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("status");

        return Result.success(status);
    }
}
