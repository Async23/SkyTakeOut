package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult selectPage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    void insert(EmployeeDTO employeeDTO);

    void startOrStop(Integer status, Integer id);

    Employee selectById(Integer id);

    void update(EmployeeDTO employeeDTO);
}
