package com.sky.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    /**
     * 纬度值
     */
    private double lat;
    /**
     * 经度值
     */
    private double lng;
}