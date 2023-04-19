package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sky.constant.PasswordConstant.DEFAULT_PASSWORD;

/**
 * @author Async_
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        //  TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、返回实体对象
        return employee;
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return PageResult
     */
    @Override
    public PageResult selectPage(EmployeePageQueryDTO employeePageQueryDTO) {
        // name 有效，忽略分页参数，根据 username 条件查询
        if (!(employeePageQueryDTO.getName() == null || "".equals(employeePageQueryDTO.getName().trim()))) {
            List<Employee> employeeList = employeeMapper.selectByName(employeePageQueryDTO.getName().trim());
            return new PageResult(employeeList.size(), employeeList);
        }

        // name 无效，分页参数无效，抛出业务异常
        if (employeePageQueryDTO.getPage() == null || employeePageQueryDTO.getPage() <= 0 || employeePageQueryDTO.getPageSize() == null || employeePageQueryDTO.getPageSize() <= 0) {
            throw new BaseException("分页参数有误");
        }

        // name 无效，分页参数有效，正常分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> employeeList = employeeMapper.selectAll();
        return new PageResult(employeeList.getTotal(), employeeList.getResult());

    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void insert(EmployeeDTO employeeDTO) {
        // employeeDTO 为 null
        if (employeeDTO == null) {
            throw new BaseException("新增员工为空");
        }

        // employeeDTO 中部分字段 为 null
        if (employeeDTO.getIdNumber() == null || employeeDTO.getIdNumber().length() == 0 || employeeDTO.getPhone() == null || employeeDTO.getPhone().length() == 0 || employeeDTO.getName() == null || employeeDTO.getName().length() == 0 || employeeDTO.getSex() == null || employeeDTO.getSex().length() == 0 || employeeDTO.getUsername() == null || employeeDTO.getUsername().length() == 0) {
            throw new BaseException("新增员工部分参数为空");
        }

        // 包装为 Employee 类型加入数据库
        Employee employee = Employee.builder().createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).createUser(BaseContext.getCurrentId()).updateUser(BaseContext.getCurrentId()).password(DigestUtils.md5DigestAsHex(DEFAULT_PASSWORD.getBytes())).build();
        // TODO: 2023/4/19 使用
        BeanUtils.copyProperties(employeeDTO, employee);

        // Mapper 层
        employeeMapper.insert(employee);
    }

    /**
     * 启用、禁用员工账号
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Integer id) {
        if (id == null || status == null || (status != 0 && status != 1)) {
            throw new BaseException("参数有误");
        }

        employeeMapper.startOrStop(status, id);
    }

    /**
     * 根据 id 查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee selectById(Long id) {
        if (id == null) {
            throw new BaseException("员工 id 参数有误");
        }

        return employeeMapper.selectById(id);
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        employeeMapper.update(employeeDTO);
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     * @return
     */
    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        // PasswordEditDTO 对象数据非法
        if (passwordEditDTO == null || passwordEditDTO.getOldPassword() == null || passwordEditDTO.getNewPassword() == null) {
            throw new BaseException(MessageConstant.PASSWORD_EDIT_FAILED);
        }

        // 若原密码不正确
        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        if (!employeeMapper.selectPasswordById(passwordEditDTO.getEmpId()).equals(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()))) {
            throw new BaseException(MessageConstant.PASSWORD_ERROR);
        }

        // 原密码正确
        passwordEditDTO.setNewPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        employeeMapper.updatePassword(passwordEditDTO);
    }

}
