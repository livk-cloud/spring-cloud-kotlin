package com.livk.config.server.v2.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * <p>
 * FileScannerUtils
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
@UtilityClass
public class FileScannerUtils {

    public static final String TYPE_CLASS = ".class";

    public static final String TYPE_YML = ".yml";

    public List<String> findFileByType(String rootPath, List<String> fileList, String fileType) {
        if (fileList == null) {
            fileList = new ArrayList<>();
        }
        File rootFile = new File(rootPath);

        if (!rootFile.isDirectory()) {
            addFile(rootFile.getPath(), fileList, fileType);
        } else {
            for (String file : Objects.requireNonNull(rootFile.list())) {
                String subFilePath = rootPath + "\\" + file;
                File subFile = new File(subFilePath);
                if (!subFile.isDirectory()) {
                    addFile(subFile.getPath(), fileList, fileType);
                } else {
                    findFileByType(subFilePath, fileList, fileType);
                }
            }
        }
        return fileList;
    }

    private void addFile(String fileName, List<String> fileList, String fileType) {
        if (fileName.endsWith(fileType)) {
            fileList.add(fileName);
        }
    }

    public static String getRealRootPath(String rootPath){
        if (System.getProperty("os.name").startsWith("Windows")
            && rootPath.startsWith("/")){
            rootPath = rootPath.substring(1);
            rootPath = rootPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        }
        return rootPath;
    }
}
