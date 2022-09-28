package com.livk.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>
 * 此接口必须继承{@link Serializable}
 * </p>
 * <p>
 * 否则出现丢失writeReplace()方法
 * </p>
 *
 * @author livk
 * @date 2022/2/25
 */
@FunctionalInterface
public interface FieldFunction<T> extends Function<T, Object>, Serializable {

}
