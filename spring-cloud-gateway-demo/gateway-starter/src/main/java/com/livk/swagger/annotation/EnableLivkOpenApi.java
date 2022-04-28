package com.livk.swagger.annotation;

import com.livk.swagger.LivkSwaggerAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * EnableLivkOpenApi
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LivkSwaggerAutoConfig.class)
public @interface EnableLivkOpenApi {

}
