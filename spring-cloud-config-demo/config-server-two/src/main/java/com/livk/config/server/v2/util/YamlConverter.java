package com.livk.config.server.v2.util;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * YamlConverter
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
public class YamlConverter {

    public static void doConvert(Map<String,Object> map, String parentKey, Map<String,Object> propertiesMap){
        String prefix=(Objects.isNull(parentKey))?"": parentKey + ".";
        map.forEach((key,value)->{
            if (value instanceof Map){
                doConvert((Map)value,prefix+key,propertiesMap);
            }else{
                propertiesMap.put(prefix+key,value);
            }
        });
    }
}
