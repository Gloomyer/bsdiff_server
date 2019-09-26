package com.gloomyer.diff;

import com.gloomyer.diff.runner.DiffUpload;
import com.gloomyer.diff.runner.impl.AndroidDiffRunnerImpl;
import com.gloomyer.diff.utils.AndroidApkDiffUtils;
import com.gloomyer.diff.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffApplicationTests {

    @Autowired
    DiffUpload mUpload;
    @Autowired
    AndroidDiffRunnerImpl mRunner;
    @Autowired
    AndroidApkDiffUtils mDiffUtils;

    @Test
    public void test() {
        mUpload.upload("", "");
    }

    @Test
    public void testRunner() {
        mDiffUtils.isSuccess();
    }

    @Test
    public void verTest() {
        Utils.canUpdate("2.0.0", "1.9.9");
    }
}
