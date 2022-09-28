package com.livk.factory.gray;

import com.livk.util.FieldUtils;

/**
 * <p>
 * GrayConstant
 * </p>
 *
 * @author livk
 * @date 2022/9/28
 */
public interface GrayConstant {

    String VERSION = FieldUtils.getFieldName(GrayGatewayFilterFactory.Config::getVersion);

    String IPS = FieldUtils.getFieldName(GrayGatewayFilterFactory.Config::getIps);
}
