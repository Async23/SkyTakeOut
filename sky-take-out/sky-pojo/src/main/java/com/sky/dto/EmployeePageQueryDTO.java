package com.sky.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// @Data
@Getter
@Setter
public class EmployeePageQueryDTO implements Serializable {

    // 员工姓名
    private String name;

    // 页码（已修改 int -> Integer）
    private Integer page;

    // 每页显示记录数（已修改 int -> Integer）
    private Integer pageSize;

}
