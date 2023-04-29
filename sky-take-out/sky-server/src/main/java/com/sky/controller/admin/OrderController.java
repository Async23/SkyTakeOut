package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageResult> listByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.listByCondition(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancel(ordersCancelDTO);

        return Result.success();
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejection(ordersRejectionDTO);

        return Result.success();
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     * @return
     */
    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单:{}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);

        return Result.success();
    }

    /**
     * 派送订单
     *
     * @param id
     * @return
     */
    @ApiOperation("派送订单")
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        log.info("派送订单 id:{}", id);
        orderService.delivery(id);

        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @ApiOperation("完成订单")
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单 id:{}", id);
        orderService.complete(id);

        return Result.success();
    }

    /**
     * 根据 id 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> selectById(@PathVariable Long id) {
        OrderVO orderVO = orderService.selectById(id);

        return Result.success(orderVO);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return OrderStatisticsVO
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }
}
