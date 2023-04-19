package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.PasswordEditDTO;
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

    @Select("select id, name,username,phone,status,update_time from employee where name like concat('%',#{name},'%')")
    List<Employee> selectByName(String name);

    @Select("select id, name,username,phone,status,update_time from employee where username = #{username}")
    Employee selectByUsername(String username);

    void insert(Employee employee);

    @Update("update employee set status=#{status} where id=#{id}")
    void startOrStop(@Param("status") Integer status, @Param("id") Integer id);

    @Select("select  id,username,name,phone,sex,id_number from employee where id=#{id}")
    Employee selectById(Long id);

    void update(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     * @return
     */
    @Update("update employee set password=#{newPassword} where id=#{empId}")
    void updatePassword(PasswordEditDTO passwordEditDTO);

    /**
     * 根据 id 查询 password
     *
     * @param id
     * @return
     */
    @Select("select password from employee where id=#{id}")
    String selectPasswordById(Long id);
}
