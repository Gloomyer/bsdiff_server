package com.gloomyer.diff.domain;

public class BaseResp<T> {


    public static <R> BaseResp<R> success(R data) {
        return new BaseResp<>("0","success", data);
    }

    private String respCode;
    private String respDesc;
    private T respData;

    public BaseResp() {
    }

    public BaseResp(String respCode, String respDesc, T respData) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.respData = respData;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public T getRespData() {
        return respData;
    }

    public void setRespData(T respData) {
        this.respData = respData;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "respCode='" + respCode + '\'' +
                ", respDesc='" + respDesc + '\'' +
                ", respData=" + respData +
                '}';
    }
}
