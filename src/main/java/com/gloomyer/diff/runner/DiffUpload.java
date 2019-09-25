package com.gloomyer.diff.runner;

import org.springframework.stereotype.Component;

/**
 * 文件上传接口
 */
public interface DiffUpload {
    boolean upload(String path,String key);
}
