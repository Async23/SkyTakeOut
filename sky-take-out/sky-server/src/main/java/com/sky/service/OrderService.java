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
    void cancel(OrdersCancelDTO ordersCancelDTO);
}
