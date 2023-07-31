package com.livk.factory.gray;

import com.livk.commons.beans.BeanLambdaFunc;

/**
 * <p>
 * GrayConstant
 * </p>
 *
 * @author livk
 * @date 2022/9/28
 */
public interface GrayConstant {

    String VERSION = BeanLambdaFunc.fieldName(GrayGatewayFilterFactory.Config::getVersion);

    String IPS = BeanLambdaFunc.fieldName(GrayGatewayFilterFactory.Config::getIps);
}
