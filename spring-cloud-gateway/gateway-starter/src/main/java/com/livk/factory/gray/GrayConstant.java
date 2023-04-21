package com.livk.factory.gray;

import com.livk.commons.function.FieldFunc;

/**
 * <p>
 * GrayConstant
 * </p>
 *
 * @author livk
 * @date 2022/9/28
 */
public interface GrayConstant {

    String VERSION = FieldFunc.getName(GrayGatewayFilterFactory.Config::getVersion);

    String IPS = FieldFunc.getName(GrayGatewayFilterFactory.Config::getIps);
}
