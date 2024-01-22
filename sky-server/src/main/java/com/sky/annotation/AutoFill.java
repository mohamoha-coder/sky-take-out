package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */

@Target(ElementType.METHOD) //添加Target注解，表示这个注解只能添加在方法上面去使用
@Retention(RetentionPolicy.RUNTIME) //一般固定写法
public @interface AutoFill {
    //指定当前数据库的操作的类型在package com.sky.enumeration下的OperationType里面，update和insert
    OperationType value();

}
