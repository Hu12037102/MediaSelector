package com.example.media.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 获取父文件夹名字
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getParentFileName(@NonNull String filePath) {
        return getParentFile(filePath).getName();
    }

    /**
     * 获取父文件夹绝对路径
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getParentFilePath(@NonNull String filePath) {
        return getParentFile(filePath).getAbsolutePath();
    }

    private static File getParentFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.getParentFile();
        }
         throw new NullPointerException("file must exists or isFile");
    }

    public static boolean existsFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile())
            return true;
        return false;
    }
}
