package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeEditPasswordDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.PasswordEncoder;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对 - 使用PasswordEncoder工具类验证密码
        if (!PasswordEncoder.matches(employee.getPassword(), password)) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employee
     */
    @AutoFill(AutoFill.OperationType.INSERT)
    public void save(EmployeeDTO employeeDTO) {
        // 检查用户名是否已存在
        Employee existingEmployee = employeeMapper.getByUsername(employeeDTO.getUsername());
        if (existingEmployee != null) {
            // 用户名已存在，抛出业务异常
            throw new BaseException("用户名已存在");
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setPassword(PasswordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));
        employee.setStatus(StatusConstant.ENABLE);
        
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        
        // 调用Mapper层的查询方法
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        
        // 封装分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @AutoFill(AutoFill.OperationType.UPDATE)
    public void updateStatus(int status, Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeMapper.updatestatus(employee);
    }
    
    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
    
    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     */
    @Override
    @AutoFill(AutoFill.OperationType.UPDATE)
    public void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        Long empId = employeeEditPasswordDTO.getEmpId();
        String oldPassword = employeeEditPasswordDTO.getOldPassword();
        String newPassword = employeeEditPasswordDTO.getNewPassword();
        
        // 根据员工ID查询员工信息
        Employee employee = employeeMapper.getById(empId);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        // 验证旧密码是否正确
        if (!PasswordEncoder.matches(employee.getPassword(), oldPassword)) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        
        // 更新密码
        Employee updateEmployee = new Employee();
        updateEmployee.setId(empId);
        updateEmployee.setPassword(PasswordEncoder.encode(newPassword));
        
        employeeMapper.updatePassword(updateEmployee);
    }

}
