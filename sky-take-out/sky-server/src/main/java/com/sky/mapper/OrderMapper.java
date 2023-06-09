package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
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

    /**
     * 获得订单总数 totalOrderCount，有效订单数 validOrderCountList，订单完成率 orderCompletionRate
     *
     * @return
     */
    Map<String, Object> selectOrderCounts(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询日期区间内、符合状态的订单数量
     *
     * @param map
     * @param status
     * @return
     */
    Integer countByMap(Map<String, LocalDateTime> map, Integer status);

    /**
     * 查询销量排名top10接口
     *
     * @return
     */
    List<Map<String, Object>> top10(LocalDateTime begin, LocalDateTime end);

    /**
     * 计算时间段内营业额
     *
     * @param map
     * @return
     */
    Double sumMap(Map<String, LocalDateTime> map);
}
