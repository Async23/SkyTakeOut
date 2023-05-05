package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     *
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */

        Map<String, LocalDateTime> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        // 营业额
        Double turnover = orderMapper.sumMap(map);

        Double unitPrice = 0.0;

        // Mapper 层：获得订单总数 totalOrderCount，有效订单数 validOrderCountList，订单完成率 orderCompletionRate
        Map<String, Object> countMap = orderMapper.selectOrderCounts(begin, end);
        // 有效订单数
        int validOrderCount = Integer.parseInt(countMap.get("validOrderCount") + "");
        // 订单总数
        int totalOrderCount = Integer.parseInt(countMap.get("totalOrderCount") + "");

        if (totalOrderCount != 0 && validOrderCount != 0) {
            // 平均客单价
            unitPrice = turnover / validOrderCount;
        }

        // 新增用户数
        Integer newUsers = userMapper.countByMap(map);


        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(Double.valueOf(countMap.get("orderCompletionRate") + ""))
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
        Map<String, LocalDateTime> dateMap = new HashMap<>();
        dateMap.put("begin", LocalDateTime.now().with(LocalTime.MIN));

        // 待接单
        Integer waitingOrders = orderMapper.countByMap(dateMap, Orders.TO_BE_CONFIRMED);

        // 待派送
        Integer deliveredOrders = orderMapper.countByMap(dateMap, Orders.CONFIRMED);

        // 已完成
        Integer completedOrders = orderMapper.countByMap(dateMap, Orders.COMPLETED);

        // 已取消
        Integer cancelledOrders = orderMapper.countByMap(dateMap, Orders.CANCELLED);

        // 全部订单
        dateMap.put("status", null);
        Integer allOrders = orderMapper.countByMap(dateMap, null);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        // Mapper 层
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        // Mapper 层
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        // Mapper 层
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        // Mapper 层
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
