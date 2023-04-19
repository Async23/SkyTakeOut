package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result test(Exception ex) {
        log.error("未知异常：{}，打印异常跟踪栈信息如下：", ex.getClass().getName());
        ex.printStackTrace();
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result baseExceptionHandler(BaseException ex) {
        log.error("捕获到业务异常：{}，打印异常跟踪栈信息：", ex.getMessage());
        ex.printStackTrace();
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获重复..异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result baseExceptionHandler(DuplicateKeyException ex) {
        String msg = ex.getCause().getMessage().split(" ")[2].replace("'", "") + MessageConstant.ALREADY_EXISTS;
        log.error("捕获到重复..异常：{}，打印异常跟踪栈信息：", msg);
        ex.printStackTrace();
        return Result.error(msg);
    }

}
