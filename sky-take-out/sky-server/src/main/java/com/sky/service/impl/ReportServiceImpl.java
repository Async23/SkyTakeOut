package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatics(LocalDate begin, LocalDate end) {
/*        // TODO: 2023/5/3 日期列表，日期之间以逗号分隔
        List<LocalDate> dateList = null;

        // TODO: 2023/5/3 营业额列表，营业额之间以逗号分隔
        List<Double> turnoverList = null;

        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();

        // TODO: 2023/5/3 日期列表，日期之间以逗号分隔
        turnoverReportVO.setDateList(dateList.toString());

        // TODO: 2023/5/3 营业额列表，营业额之间以逗号分隔
        turnoverReportVO.setTurnoverList(turnoverList.toString());*/

        // ----------------------------------------
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            // ⽇期计算，获得指定⽇期后 1 天的⽇期 dateList.add(begin);
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // LocalDate => LocalDateTime
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);

            // Mapper 层
            Double turnover = orderMapper.sumByMap(map);

            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        // 数据封装
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        dateList.forEach(localDate -> {
            Map<String, LocalDateTime> map = new HashMap<>();
            map.put("end", LocalDateTime.of(localDate, LocalTime.MAX));
            totalUserList.add(userMapper.countByMap(map));

            map.put("begin", LocalDateTime.of(localDate, LocalTime.MIN));
            newUserList.add((userMapper.countByMap(map)));
        });

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ','))
                .totalUserList(StringUtils.join(totalUserList, ','))
                .newUserList(StringUtils.join(newUserList, ','))
                .build();
    }

    /**
     * 订单统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        // Mapper 层：获得订单总数 totalOrderCount，有效订单数 validOrderCountList，订单完成率 orderCompletionRate
        Map<String, Object> countMap = orderMapper.selectOrderCounts();
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            Map<String, LocalDateTime> map = new HashMap<>();
            map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
            map.put("end", LocalDateTime.of(begin, LocalTime.MAX));

            // Mapper 层：有效订单
            validOrderCountList.add(orderMapper.countByMap(map, Orders.COMPLETED));
            // Mapper 层：订单总数
            orderCountList.add(orderMapper.countByMap(map, null));

            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        return OrderReportVO.builder()
                // 日期
                .dateList(StringUtils.join(dateList, ','))
                // 每日订单数
                .orderCountList(StringUtils.join(orderCountList, ','))
                // 每日有效订单数
                .validOrderCountList(StringUtils.join(validOrderCountList, ','))
                // 订单总数
                .totalOrderCount(Integer.valueOf(countMap.get("totalOrderCount") + ""))
                // 有效订单数
                .validOrderCount(Integer.valueOf(countMap.get("validOrderCount") + ""))
                // 订单完成率
                .orderCompletionRate(Double.valueOf(countMap.get("orderCompletionRate") + ""))
                .build();
    }

    /**
     * 查询销量排名top10接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        if (begin == null || end == null) {
            // 查询销量排名top10参数有误
            throw new BaseException(MessageConstant.TOP10_ILLEGAL_ARGUMENT);
        }
        List<Map<String, Object>> top10List = orderMapper.top10(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        top10List.forEach(map -> {
            nameList.add(map.get("name") + "");
            numberList.add(Integer.valueOf(map.get("number") + ""));
        });

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ','))
                .numberList(StringUtils.join(numberList, ','))
                .build();
    }

}
