package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")

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
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工信息
     */

    @PostMapping() //在接口文档上是属于post类型的，需要加上这个注解
    //因为最上面加过了@RequestMapping("admin/employee")，所以这里的PostMapping不需要补充路径了
    @ApiOperation("新增员工信息")
    public Result save(@RequestBody EmployeeDTO employeeDTO){

        System.out.println("当前线程的id:" + Thread.currentThread().getId());

        //因为前端需要返回的是json格式的数据，所以传参是的前面需要加上@RequestBody的注解

        log.info("新增员工: {}", employeeDTO); //{}是一个占位符，会将后面的参数动态的拼接到后面

        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page") //观察接口文档，是get请求方法
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        //本来是要放进去三个参数， name员工姓名，page页码，pageSize每页显示记录数，EmployeePageQueryDTO封装了这三个属性
        log.info("员工分页查询，参数为: {}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    //对于查询类加上泛型<T>, 如果不是查询类的方法就可以不加了,主要是保证data的类型， 如果只是返回code就没有必要了
    //观察接口文档， 路径为https://yapi.pro/mock/234656/admin/employee/status/{status}
    //出现的有{status}作为路径参数，所以在传参的过程中需要加上一个注解PathVariable，通过这个注解来获取status参数的值，并补全上面PostMapping里的路径

    public Result StartOrStop(@PathVariable Integer status, Long id){
        log.info("启用禁用员工账号：{} , {}", status, id);

        employeeService.StartOrStop(status, id);
        return Result.success();
    }


}






































