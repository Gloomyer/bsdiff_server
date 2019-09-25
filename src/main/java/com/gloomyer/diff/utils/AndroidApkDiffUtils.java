package com.gloomyer.diff.utils;

public class AndroidApkDiffUtils {
    public static native int diff(String oldApkPath, String newApkPath, String outputPatchPath);
}
