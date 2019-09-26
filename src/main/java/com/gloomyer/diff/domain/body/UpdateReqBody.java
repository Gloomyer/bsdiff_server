package com.gloomyer.diff.domain.body;

public class UpdateReqBody {
    private String key;
    private String version;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
