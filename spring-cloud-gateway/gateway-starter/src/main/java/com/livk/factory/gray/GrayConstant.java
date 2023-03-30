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

    String VERSION = FieldFunc.get(GrayGatewayFilterFactory.Config::getVersion);

    String IPS = FieldFunc.get(GrayGatewayFilterFactory.Config::getIps);
}
