package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Async_
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api("CommonController ╮（╯＿╰）╭")
public class CommonController {
    @Autowired
    private CommonService commonService;

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件 (＠_＠;)")
    public Result upload(MultipartFile file) throws IOException {
        String url = commonService.upload(file);

        return Result.success(url);
    }
}
