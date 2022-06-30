package com.livk.dynamic.annotation;

import com.livk.dynamic.LivkDynamicAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * EnableLivkDynamic
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LivkDynamicAutoConfig.class)
public @interface EnableLivkDynamic {

}
