package com.gloomyer.diff;

import com.gloomyer.diff.utils.AndroidApkDiffUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class DiffApplication {

    public static void main(String[] args) {

        if (AndroidApkDiffUtils.isSuccess()) {
            int ret = AndroidApkDiffUtils.diff(
                    new File("1.txt").getAbsolutePath(),
                    new File("2.txt").getAbsolutePath(),
                    new File("patch.patch").getAbsolutePath()
            );
            System.out.println("result:" + ret);
        }
        SpringApplication.run(DiffApplication.class, args);
    }

}
