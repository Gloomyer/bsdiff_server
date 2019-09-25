package com.gloomyer.diff.runner.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gloomyer.diff.runner.DiffUpload;
import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 七牛上传接口
 */
@Component("diffUpload")
public class QNDiffUploadImpl implements DiffUpload {

    private static Configuration cfg;
    private static UploadManager uploadManager;

    @Value("${QNAccessKey}")
    private String accessKey;
    @Value("${QNSecretKey}")
    private String secretKey;
    @Value("${QNAccessKey}")
    private String QNBucket;

    @Override
    public boolean upload(String path, String key) {

        System.out.println(toString() + path + key);

        if (cfg == null) {
            cfg = new Configuration(Region.autoRegion());
            uploadManager = new UploadManager(cfg);
        }

        if (uploadManager == null) return false;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(QNBucket);

        try {
            Response response = uploadManager.put(path, key, upToken);
            String map = response.bodyString();
            return map.contains("hash") && map.contains("key");
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String toString() {
        return "QNDiffUploadImpl{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", QNBucket='" + QNBucket + '\'' +
                '}';
    }
}
