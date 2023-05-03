package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     *
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber
     * @param userId
     */
    Orders selectByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     *
     * @param order
     */
    void update(Orders order);

    /**
     * 订单条件搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    // TODO: 2023/4/29  
    List<OrderVO> listByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据 id 查询订单详情
     *
     * @param id
     * @return OrderVO
     */
    OrderVO selectById(Long id);

    /**
     * 各个状态的订单数量统计
     *
     * @return OrderStatisticsVO
     */
    OrderStatisticsVO statistics();

    /**
     * 根据状态和下单时间查询订单
     *
     * @param status
     * @param orderTime
     */
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据动态条件统计营业额
     *
     * @param map
     */
    Double sumByMap(Map<String, Object> map);
}
