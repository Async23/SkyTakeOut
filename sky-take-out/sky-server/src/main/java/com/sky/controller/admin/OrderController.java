package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "管理端订单接口")
@RequestMapping("/admin/order")
@RestController("adminOrderController")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单条件搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation("订单搜索")
    @GetMapping("/conditionSearch")
    public Result listByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.listByCondition(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    // TODO: 2023/4/29
    /*@GetMapping("/statistics")
    public Result statistics() {
        return null;
    }*/
}