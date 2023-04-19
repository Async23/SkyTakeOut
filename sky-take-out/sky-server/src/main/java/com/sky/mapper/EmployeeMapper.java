package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 查询所有员工
     *
     * @return
     */
    @Select("select id, name,username,phone,status,update_time from employee")
    Page<Employee> selectAll();

    @Select("select id, name,username,phone,status,update_time from employee where username like concat('%',#{username},'%')")
    List<Employee> selectByUsername(String username);

    @Select("select id, name,username,phone,status,update_time from employee where username = #{username}")
    Employee selectByUsernameOne(String username);

    void insert(Employee employee);

    @Update("update employee set status=#{status} where id=#{id}")
    void startOrStop(@Param("status") Integer status, @Param("id") Integer id);

    @Select("select  id,username,name,phone,sex,id_number from employee where id=#{id}")
    Employee selectById(Integer id);

    void update(EmployeeDTO employeeDTO);
}
