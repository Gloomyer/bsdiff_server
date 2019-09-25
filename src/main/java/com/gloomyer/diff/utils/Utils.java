package com.gloomyer.diff.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String md5Salt(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 同步下载文件
     *
     * @param networkUrl 文件url
     * @param savePath   要保存的位置
     * @return 是否下载成功
     */
    public static boolean downloadFile(String networkUrl, File savePath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(networkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                bos = new BufferedOutputStream(new FileOutputStream(savePath));

                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                    bos.flush();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (savePath.exists()) {
                savePath.delete();
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
