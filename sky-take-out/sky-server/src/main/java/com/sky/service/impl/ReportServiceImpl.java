package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
    // ----------------------------------------
    // return turnoverReportVO;
}
