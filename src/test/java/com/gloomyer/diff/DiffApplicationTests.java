package com.gloomyer.diff;

import com.gloomyer.diff.runner.AndroidDiffRunner;
import com.gloomyer.diff.runner.DiffUpload;
import com.gloomyer.diff.runner.impl.AndroidDiffRunnerImpl;
import com.gloomyer.diff.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffApplicationTests {

    @Autowired
    DiffUpload mUpload;
    @Autowired
    AndroidDiffRunnerImpl mRunner;

    @Test
    public void test() {
        mUpload.upload("", "");
    }

    @Test
    public void testRunner() {
        Utils.downloadFile("https://gloomyer.com/img/img/avatar.png",
                new File("/Users/gloomy/Downloads/apkCache/avatar.png"));
        mRunner.clearCache();
    }
}
