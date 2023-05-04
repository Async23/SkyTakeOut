package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Async
 */
@Mapper
public interface UserMapper {
    /**
     * 新用户直接插入
     *
     * @param user
     */
    void insert(User user);

    // TODO: 2023/4/25 #{}${}

    /**
     * 根据 openid 获取用户数据
     *
     * @param openid
     * @return
     */
    User getByOpenid(String openid);

    User getById(Long userId);

    /**
     * 根据起止日期计算数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map<String, LocalDateTime> map);
}
