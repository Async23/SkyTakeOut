package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sky.exception.BaseException;
import com.sky.properties.ShopProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BaiduMapUtil {
    // 获取配置类中的值
    @Value("${sky.shop.address}")
    private String myShopAddress;
    @Value("${sky.shop.ak}")
    private String ak;

    @Autowired
    private ShopProperties shopProperties;

    /**
     * 获取经纬度
     *
     * @param userAddress 北京市海淀区上地十街10号
     * @return
     */
    public Location getLocation(String userAddress) {
        String URL = "https://api.map.baidu.com/geocoding/v3";
        Map<String, String> map = new HashMap<>();
        map.put("address", userAddress);
        map.put("output", "json");
        map.put("ak", shopProperties.getAk());
        String body = HttpClientUtil.doGet(URL, map);
        Location location = new Location();
        try {
            JSONObject jsonObject = JSON.parseObject(body);
            // JSONObject jsonObject = new JSONObject(body);
            // 获取Status
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                // 解析JSON
                JSONObject res = jsonObject.getJSONObject("result").getJSONObject("location");
                // 获取经度
                String lng = res.getString("lng");
                Double transferLnf = Double.parseDouble(lng);
                location.setLng(transferLnf);

                // 获取纬度
                String lat = res.getString("lat");
                Double transferLat = Double.parseDouble(lat);
                location.setLat(transferLat);
            } else {
                // 如果没有返回排除异常交给全局异常处理
                throw new BaseException("无权限");
            }
        } catch (Exception e) {
            log.info("解析JSON异常,异常信息{}", e.getMessage());
        }
        return location;
    }

    /**
     * 通过两个经纬度信息判断,返回距离信息
     *
     * @return 二者的距离
     */
    public String getDistance(Location userLocation) {
        Location myShopLocation = getLocation(shopProperties.getAddress());
        // 起始位置, 即我的位置
        String origin = myShopLocation.getLat() + "," + myShopLocation.getLng();
        // 最终位置, 即终点
        String destination = userLocation.getLat() + "," + userLocation.getLng();

        String url = "https://api.map.baidu.com/directionlite/v1/driving";
        // String url = "https://api.map.baidu.com/directionlite/v1/riding";
        // 发送Get请求
        HashMap<String, String> map = new HashMap<>();
        map.put("origin", origin);
        map.put("destination", destination);
        map.put("ak", shopProperties.getAk());
        map.put("steps_info", "0");
        String result = HttpClientUtil.doGet(url, map);
        String distance = null;
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            distance = jsonObject.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getString("distance");
        } catch (JSONException e) {
            log.info("路径异常");
        }
        log.info("二者距离{}", distance);
        return distance;
    }
}


