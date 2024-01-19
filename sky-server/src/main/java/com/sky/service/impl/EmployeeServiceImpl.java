package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

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

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //对前端传过来的password进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
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

    @Override
    public void save(EmployeeDTO employeeDTO) {

        System.out.println("当前线程的id:" + Thread.currentThread().getId());


        Employee employee = new Employee();

        // employee.setName(employeeDTO.getName());
        // 可以采用这种写法，但是属性过多会导致十分繁杂
        // 这里采用对象属性拷贝的方法 使用BeanUtils.copyProperties()方法并传入两个参数，第一个是拷贝源，第二个是拷贝结果
        BeanUtils.copyProperties(employeeDTO, employee); //employeeDTO是拷贝源，employee是拷贝结果

        //然后补充employee后面剩下的一些属性

        //设置账号状态，初始默认全部为正常, 1为正常，0为锁定
        //employee.setStatus(1); 设置为1就属于是硬编码，不方便后期的修改和维护,那么就用一个常量进去，在外部方便修改
        employee.setStatus(StatusConstant.ENABLE);


        //设置密码，默认密码为123456，并且需要进行md5加密后放入数据库,同时也设置了一个外部的常量，方便后期修改
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));


        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人id和修改人id, 目前先写死后面改
        //TODO 后期需要改为当前登录用户的id
        employee.setCreateUser(BaseContext.getCurrentId()); //long类型，后面+L
        employee.setUpdateUser(BaseContext.getCurrentId());


        //调用注入的employeeMapper的insert方法
        employeeMapper.insert(employee);
    }

}






















