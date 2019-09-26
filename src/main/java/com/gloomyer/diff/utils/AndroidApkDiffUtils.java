package com.gloomyer.diff.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

@Component("androidApkDiffUtils")
public class AndroidApkDiffUtils {
    private static int status;

    @Value("${soDir}")
    private String soDir;

    /**
     * 判断so是否加载成功!
     *
     * @return 是否加载成功!
     */
    public boolean isSuccess() {
        if (status == 0) {
            load();
            return isSuccess();
        }
        return status == 1;
    }

    /**
     * 加载so文件
     */
    private void load() {
        try {
            File soFile;
            {
                //so dir 不存在 创建一下
                soFile = new File(soDir);
                if (!soFile.exists()) {
                    soFile.mkdirs();
                }
            }

            boolean isMacOs = System.getProperty("os.name").toLowerCase().contains("mac");
            ClassPathResource resource = new ClassPathResource("jniLibs" + File.separator
                    + (isMacOs ? "mac" : "linux") + File.separator
                    + "libGdiff.so");

            soFile = new File(soDir, "libGdiff.so");

            if (!soFile.exists()) {
                //copy so 到指定目录 存储so
                InputStream is = resource.getInputStream();
                FileOutputStream fos = new FileOutputStream(soFile);
                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }

                fos.close();
                is.close();
            }
            //加载so
            System.load(soFile.getAbsolutePath());
            status = 1;
        } catch (Throwable e) {
            e.printStackTrace();
            status = 2;
        }
        System.out.println("success:" + isSuccess());
    }


    public static native int diff(String oldApkPath, String newApkPath, String outputPatchPath);

}
