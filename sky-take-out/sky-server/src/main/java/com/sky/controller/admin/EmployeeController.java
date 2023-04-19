package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.BaseException;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;


/**
 * 员工管理
 * <p>
 * /api/ 映射为 /admin/
 *
 * @author Async_
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "EmployeeController (●ˇ∀ˇ●)")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        log.info("返回 token：{}", token);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder().id(employee.getId()).userName(employee.getUsername()).name(employee.getName()).token(token).build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return PageResult
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询 ♪(^∇^*)")
    // TODO: 2023/4/19 如何注入
    public Result<PageResult> selectPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询参数：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.selectPage(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工 O(∩_∩)O")
    public Result insert(@RequestBody EmployeeDTO employeeDTO) {
        // Service 层
        employeeService.insert(employeeDTO);

        return Result.success();
    }

    /**
     * 启用、禁用员工账号
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号 ┭┮﹏┭┮")
    public Result startOrStop(@PathVariable Integer status, Integer id) {
        // Service 层
        employeeService.startOrStop(status, id);

        return Result.success();
    }

    /**
     * 根据 id 查询员工
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询员工 (●'◡'●)")
    public Result selectById(@PathVariable Integer id) {
        Employee employee = employeeService.selectById(id);

        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑员工信息 o(TヘTo)")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.update(employeeDTO);

        return Result.success();
    }
}
