package com.livk.config.server.v2.support;

import com.livk.config.server.v2.util.FileScannerUtils;
import com.livk.config.server.v2.util.VariablePool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * <p>
 * ScanRunner
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
@Component
public class ScanRunner implements CommandLineRunner {
    @Override
    public void run(final String... args) throws Exception {
        String rootPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        List<String> fileList = FileScannerUtils.findFileByType(rootPath, null, FileScannerUtils.TYPE_CLASS);
        doFilter(rootPath, fileList);

    }

    private void doFilter(String rootPath, List<String> fileList) {
        rootPath = FileScannerUtils.getRealRootPath(rootPath);
        for (String filePath : fileList) {
            String shortName = filePath.replace(rootPath, "")
                    .replace(FileScannerUtils.TYPE_CLASS, "");
            String packageFileName = shortName.replaceAll(Matcher.quoteReplacement(File.separator), "\\.");

            try {
                Class<?> clazz = Class.forName(packageFileName);
                if (clazz.isAnnotationPresent(Component.class) ||
                    clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Controller.class)) {
                    VariablePool.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
