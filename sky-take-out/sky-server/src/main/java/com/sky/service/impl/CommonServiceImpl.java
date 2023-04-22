package com.sky.service.impl;

import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * "oss-cn-shanghai.aliyuncs.com"
 * "LTAI5tK8B7TAVqcafVpZsptX"
 * "3Z5B4tsRYhIW0gp5ODKinImZkujKn0"
 * "async-sky-take-out"
 */

/**
 * @author Async_
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    // TODO: 2023/4/20 优化属性注入方式
    private AliOssUtil aliOssUtil = new AliOssUtil(
            "oss-cn-shanghai.aliyuncs.com",
            "LTAI5tDDFZ3DV65PG3xm1tFk",
            "xP4iGwg8TJtdLa1Cmm6PUL93E9F9lV",
            "web-tlias-zmq");

    @Override
    public String upload(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        String name = file.getOriginalFilename();
        return aliOssUtil.upload(fileBytes, name);
    }
}
