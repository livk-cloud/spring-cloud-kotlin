package com.livk.dynamic.annotation;

import com.livk.commons.spring.context.AutoImport;

import java.lang.annotation.*;

/**
 * <p>
 * EnableDynamicGateway
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoImport
public @interface EnableDynamicGateway {

}
