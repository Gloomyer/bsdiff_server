package com.gloomyer.diff.utils;

import com.gloomyer.diff.DiffApplication;

import java.io.File;
import java.net.URL;

public class AndroidApkDiffUtils {
    private static boolean success;

    /**
     * 是否成功的加载了so对象
     *
     * @return 是否成功
     */
    public static boolean isSuccess() {
        return success;
    }

    static {
        try {
            boolean isMacOs = System.getProperty("os.name").toLowerCase().contains("mac");
            File soPath = new File(DiffApplication.class.getResource("/").getFile());
            soPath = new File(soPath, "jniLibs");
            if (isMacOs) {
                soPath = new File(soPath, "mac");
            } else {
                soPath = new File(soPath, "linux");
            }
            soPath = new File(soPath, "libGdiff.so");
            System.out.println(soPath);
            System.load(soPath.getAbsolutePath());
            success = true;
        } catch (Throwable e) {
            e.printStackTrace();
            success = false;
        }
        System.out.println("success:" + success);
    }

    public static native int diff(String oldApkPath, String newApkPath, String outputPatchPath);

}
