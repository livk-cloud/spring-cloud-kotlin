package com.livk.config.server.v2.support;

import com.livk.config.server.v2.util.FileScannerUtils;
import com.livk.config.server.v2.util.YamlConverter;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * EnvInitializer
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
public class EnvInitializer {
    private static Map<String,Object> envMap=new HashMap<>();

    public static void init(){
        String rootPath = EnvInitializer.class.getResource("/").getPath();
        List<String> fileList = FileScannerUtils.findFileByType(rootPath,null,FileScannerUtils.TYPE_YML);
        for (String ymlFilePath : fileList) {
            rootPath = FileScannerUtils.getRealRootPath(rootPath);
            ymlFilePath = ymlFilePath.replace(rootPath, "");
            YamlMapFactoryBean yamlMapFb = new YamlMapFactoryBean();
            yamlMapFb.setResources(new ClassPathResource(ymlFilePath));
            Map<String, Object> map = yamlMapFb.getObject();
            YamlConverter.doConvert(map,null,envMap);
        }
    }

    public static void setEnvMap(Map<String, Object> envMap) {
        EnvInitializer.envMap = envMap;
    }
    public static Map<String, Object> getEnvMap() {
        return envMap;
    }
}
