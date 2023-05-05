package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatics(LocalDate begin, LocalDate end) {
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
        Map<String, Object> countMap = orderMapper.selectOrderCounts(
                LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX)
        );
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

    /**
     * 导出Excel报表接口
     *
     * @return
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        // 查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(
                LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            // 获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            // 获得第4行
            XSSFRow row = sheet.getRow(3);
            // 获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                // 准备明细数据
                businessData = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            // 通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // 关闭资源
            out.flush();
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
