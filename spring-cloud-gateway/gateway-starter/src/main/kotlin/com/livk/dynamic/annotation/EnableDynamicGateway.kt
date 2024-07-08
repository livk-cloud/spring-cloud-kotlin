package com.livk.dynamic.annotation

import com.livk.commons.selector.AutoImport
import java.lang.annotation.Inherited

/**

 * @author livk
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@AutoImport
annotation class EnableDynamicGateway
