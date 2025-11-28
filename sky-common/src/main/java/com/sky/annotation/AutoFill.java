package com.sky.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解，用于标识需要进行公共字段自动填充的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 操作类型：INSERT（新增）、UPDATE（更新）
    OperationType value();

    // 操作类型枚举
    enum OperationType {
        INSERT,
        UPDATE
    }
}