package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Async_
 */
public interface CommonService {
    String upload(MultipartFile file) throws IOException;
}
