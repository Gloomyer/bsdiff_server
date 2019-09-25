package com.gloomyer.diff.runner;


import com.gloomyer.diff.runner.impl.AndroidDiffRunnerImpl;
import com.gloomyer.diff.utils.SpringUtil;

import java.util.HashMap;
import java.util.Map;

public class AndroidApkDiffRunnerFactory {


    public enum MapInstance {
        INS;
        public Map<String, AndroidDiffRunner> runnerMap;

        MapInstance() {
            runnerMap = new HashMap<>();
        }
    }

    /**
     * 押入一个Android差分生成任务
     *
     * @param newApkKey    当前最新版本apk key
     * @param oldApkKey    请求提交的apk key
     * @param patchKey     差分key
     * @return 押入成功返回runner， 押入相同任务返回null
     */
    public static AndroidDiffRunner put(String newApkKey, String oldApkKey, String patchKey) {
        AndroidDiffRunnerImpl impl = null;
        synchronized (AndroidApkDiffRunnerFactory.class) {
            if (!MapInstance.INS.runnerMap.containsKey(patchKey)) {
                impl = SpringUtil.getBean("androidDiffRunner", AndroidDiffRunnerImpl.class);
                impl.setNewApkKey(newApkKey);
                impl.setOldApkKey(oldApkKey);
                impl.setPatchKey(patchKey);
                MapInstance.INS.runnerMap.put(patchKey, impl);
            }
        }
        return impl;
    }
}
