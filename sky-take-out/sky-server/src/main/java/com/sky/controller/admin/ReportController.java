package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计相关
 */
@Slf4j
@RestController
@Api(tags = "数据统计相关接口")
@RequestMapping("/report")
public class ReportController {
    @ApiOperation("营业额统计")
    public Result turnoverStatics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        // TODO: 2023/5/3 日期列表，日期之间以逗号分隔
        List<LocalDate> dateList = null;

        // TODO: 2023/5/3 营业额列表，营业额之间以逗号分隔
        List<Double> turnoverList = null;

        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();

        // TODO: 2023/5/3 日期列表，日期之间以逗号分隔
        turnoverReportVO.setDateList(dateList.toString());

        // TODO: 2023/5/3 营业额列表，营业额之间以逗号分隔
        turnoverReportVO.setTurnoverList(turnoverList.toString());

        return Result.success(turnoverReportVO);
    }
}
