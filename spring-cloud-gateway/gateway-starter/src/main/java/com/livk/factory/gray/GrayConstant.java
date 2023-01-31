package com.livk.factory.gray;

import com.livk.commons.util.ReflectionUtils;

/**
 * <p>
 * GrayConstant
 * </p>
 *
 * @author livk
 * @date 2022/9/28
 */
public interface GrayConstant {

    String VERSION = ReflectionUtils.getFieldName(GrayGatewayFilterFactory.Config::getVersion);

    String IPS = ReflectionUtils.getFieldName(GrayGatewayFilterFactory.Config::getIps);
}
