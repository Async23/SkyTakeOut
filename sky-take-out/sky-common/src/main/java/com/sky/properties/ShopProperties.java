package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sky.shop")
public class ShopProperties {
    /**
     * 商家地址
     */
    private String address;

    /**
     * 百度接口参数
     */
    private String ak;

    /**
     * 上限配送距离
     */
    private String distance;
}
