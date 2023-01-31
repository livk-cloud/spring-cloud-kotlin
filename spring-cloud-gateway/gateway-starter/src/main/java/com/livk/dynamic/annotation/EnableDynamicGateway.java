package com.livk.dynamic.annotation;

import com.livk.dynamic.DynamicGatewayImportSelect;
import org.springframework.context.annotation.Import;

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
@Import(DynamicGatewayImportSelect.class)
public @interface EnableDynamicGateway {

}
