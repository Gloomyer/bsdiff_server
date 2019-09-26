package com.gloomyer.diff.runner.impl;

import com.gloomyer.diff.enums.AndroidTaskStatus;
import com.gloomyer.diff.runner.AndroidApkDiffRunnerFactory;
import com.gloomyer.diff.runner.AndroidDiffRunner;
import com.gloomyer.diff.runner.DiffUpload;
import com.gloomyer.diff.utils.AndroidApkDiffUtils;
import com.gloomyer.diff.utils.SpringUtil;
import com.gloomyer.diff.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.concurrent.*;

/**
 * 差分任务实际执行者
 */
@Component("androidDiffRunner")
public class AndroidDiffRunnerImpl implements Runnable, AndroidDiffRunner {


    private enum ExecutorInstance {
        INS;

        ThreadPoolExecutor mExecutor;

        private static final int CORE_POOL_SIZE = 1;
        private static final int MAX_POOL_SIZE = 1;

        ExecutorInstance() {
            mExecutor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAX_POOL_SIZE,
                    5,
                    TimeUnit.MINUTES,
                    new ArrayBlockingQueue<>(MAX_POOL_SIZE),
                    (r, executor) -> {
                        //向数据写入runner
                        AndroidDiffRunnerImpl impl = (AndroidDiffRunnerImpl) r;
                        impl.setStatus(AndroidTaskStatus.FAIL);
                    }
            );
        }
    }


    @Value("${diff_update.cache_file}")
    private String cacheDir;
    @Value("${preUrl}")
    private String preUrl;
    @Value("${maxCacheTime}")
    private Long maxCacheTime;

    private String newApkKey;
    private File newApkFile;
    private String oldApkKey;
    private File oldApkFile;
    private String patchKey;
    private File patchFile;

    public AndroidDiffRunnerImpl() {

    }

    public void setNewApkKey(String newApkKey) {
        this.newApkKey = newApkKey;
        this.newApkFile = new File(new File(cacheDir), newApkKey);
    }

    public void setOldApkKey(String oldApkKey) {
        this.oldApkKey = oldApkKey;
        this.oldApkFile = new File(new File(cacheDir), oldApkKey);
    }

    public void setPatchKey(String patchKey) {
        this.patchKey = patchKey;
        this.patchFile = new File(new File(cacheDir), patchKey);
    }

    @Override
    public void run() {
        File cache = new File(cacheDir);
        if (!cache.exists()) {
            cache.mkdirs();
        }

        setStatus(AndroidTaskStatus.RUNING);
        System.out.println(toString());

        //先下载最新的包
        //如果没有缓存就去下载
        if (!newApkFile.exists()) {
            String newApkUrl = MessageFormat.format(preUrl, newApkKey);
            if (!Utils.downloadFile(newApkUrl, newApkFile)) {
                setStatus(AndroidTaskStatus.FAIL);
                return;
            }
        }

        //下载老版本的包
        //如果没有缓存就去下载
        if (!oldApkFile.exists()) {
            String newApkUrl = MessageFormat.format(preUrl, oldApkKey);
            if (!Utils.downloadFile(newApkUrl, oldApkFile)) {
                setStatus(AndroidTaskStatus.FAIL);
                return;
            }
        }

        //合成文件
        if (AndroidApkDiffUtils.diff(
                oldApkFile.getAbsolutePath(),
                newApkFile.getAbsolutePath(),
                patchFile.getAbsolutePath()) == 0) {
            //去上传文件
            DiffUpload mUpload = SpringUtil.getBean("diffUpload", DiffUpload.class);
            if (mUpload.upload(patchFile.getAbsolutePath(), patchKey)) {
                setStatus(AndroidTaskStatus.SUCCESS);
                return;
            }
        }
        if (patchFile.exists()) {
            patchFile.delete();
        }
        clearCache();
        setStatus(AndroidTaskStatus.FAIL);
    }

    /**
     * 清理缓存文件
     */
    public void clearCache() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        String fileName = MessageFormat.format("{0}-{1}-{2}.lock", y, m, d);
        File flagFile = new File(cacheDir, fileName);
        if (!flagFile.exists()) {
            try {
                flagFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            File cacheDir = new File(this.cacheDir);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    System.out.println(f.lastModified());
                    if (f.lastModified() <=
                            (calendar.get(Calendar.MILLISECOND) - (maxCacheTime * 24 * 60 * 1000 * 1000))) {
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 创建标志文件
     *
     * @param file 文件路径
     */
    private void touchFile(File file) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write("touch".getBytes());
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start() {
        //押入线程池
        ExecutorInstance.INS.mExecutor.execute(this);
    }


    /**
     * 设置key的app状态
     */
    private void setStatus(AndroidTaskStatus status) {
        //TODO 修改数据库状态


        if (status == AndroidTaskStatus.SUCCESS
                || status == AndroidTaskStatus.FAIL) {
            //从任务列表中移除
            AndroidApkDiffRunnerFactory.MapInstance.INS.runnerMap.remove(patchKey);
        }
    }

    @Override
    public String toString() {
        return "AndroidDiffRunnerImpl{" +
                "newApkKey='" + newApkKey + '\'' +
                ", oldApkKey='" + oldApkKey + '\'' +
                ", patchKey='" + patchKey + '\'' +
                ", cacheDir='" + cacheDir + '\'' +
                ", maxCacheTime=" + maxCacheTime +
                ", preUrl='" + preUrl + '\'' +
                '}';
    }
}
