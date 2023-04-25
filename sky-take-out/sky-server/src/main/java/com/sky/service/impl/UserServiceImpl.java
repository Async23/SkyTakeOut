package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Async
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * 微信服务接⼝地址
     */
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        // 判断 openid 是否为空，如果为空表示登录失败，抛出业务异常
        if (openid == null) {
            // "登录失败"
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // Mapper 层：判断当前⽤户是否为新⽤户
        User user = userMapper.getByOpenid(openid);
        // 如果是新⽤户，⾃动完成注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            // Mapper 层：后绪步骤实现
            userMapper.insert(user);
        }
        // 返回这个⽤户对象
        return user;
    }

    /**
     * 调⽤微信接⼝服务，获取微信⽤户的openid
     *
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        // 调⽤微信接⼝服务，获得当前微信⽤户的 openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
