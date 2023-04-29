package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.*;

public interface OrderService {

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单条件搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult listByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    void adminCancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 根据 id 查询订单详情
     *
     * @param id
     * @return
     */
    OrderVO selectById(Long id);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     * @return
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return OrderStatisticsVO
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     * @return
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 派送订单
     *
     * @param id
     * @return
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id
     * @return
     */

    void complete(Long id);

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户取消订单
     *
     * @param id
     * @return
     */
    void cancel(Long id);
}
