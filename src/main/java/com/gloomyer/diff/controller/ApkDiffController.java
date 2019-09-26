package com.gloomyer.diff.controller;

import com.gloomyer.diff.domain.BaseResp;
import com.gloomyer.diff.domain.UpdateBean;
import com.gloomyer.diff.domain.body.UpdateReqBody;
import com.gloomyer.diff.enums.AndroidTaskStatus;
import com.gloomyer.diff.runner.AndroidApkDiffRunnerFactory;
import com.gloomyer.diff.runner.AndroidDiffRunner;
import com.gloomyer.diff.utils.AndroidApkDiffUtils;
import com.gloomyer.diff.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApkDiffController {

    @Value("${currentApkKey}")
    private String currentApkKey;
    @Value("${currentAppVersion}")
    private String currentAppVersion;

    @Autowired
    AndroidApkDiffUtils mDiffUtils;

    @RequestMapping(path = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public BaseResp<UpdateBean> update(@RequestBody UpdateReqBody body) {
        UpdateBean info = new UpdateBean();


        //如果Android没有给key， 那么不走差分逻辑 如果差分工具没有加载成功也不走差分任务 还需要判断是Android客户端请求的
        if (!StringUtils.isEmpty(body.getKey())
                && !currentApkKey.equals(body.getKey())
                && mDiffUtils.isSuccess()
                && Utils.canUpdate(body.getVersion(), currentAppVersion)) {
            //1:从redis 或者数据库中获取最新的apk的key！,我这里从配置文件中读取了
            System.out.println("服务端最新版本的版本key:" + currentApkKey);

            //请求参数版本key
            System.out.println("Android客户端提交的版本key:" + body.getKey());

            //利用Android端请求提交的key 和 最新的key生成差分包的key
            String diffPatchKey = Utils.md5Salt(currentApkKey + body.getKey());
            System.out.println("差分包key：" + diffPatchKey);

            //根据差分key 从redis 或者 mysql 获取 2个字段 status & url
            //mock
            int status = 0; //状态含义,0:未开始，1:执行中,2:已经完成,3:执行过了，但是失败了.
            String patchUrl = "http://google.com";

            if (status == AndroidTaskStatus.NEVER_STARTED.ordinal()) {
                //提交差分任务
                //考虑并发情况， 这里直接押入，下层处理相同任务问题
                AndroidDiffRunner runner = AndroidApkDiffRunnerFactory
                        .put(currentApkKey, body.getKey(), diffPatchKey);
                if (runner != null) {
                    //说明没有并发任务
                    runner.start();
                }
            } else if (status == AndroidTaskStatus.SUCCESS.ordinal()) {
                //提取url放入返回结构中
                info.setDiffPatchUrl(patchUrl);
            }
        }

        info.setId(1);
        info.setPlatform("Android");
        info.setUpdateTime(System.currentTimeMillis() + "");
        info.setCreateTime(info.getUpdateTime());
        info.setVersion("1.2.0");
        info.setUrl("http://baidu.com");
        return BaseResp.success(info);
    }

    @RequestMapping(path = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String test() {
        int ret = -1;
        if (mDiffUtils.isSuccess()) {
            ret = AndroidApkDiffUtils.diff(
                    "/root/1.txt",
                    "/root/2.txt",
                    "/root/patch.patch"
            );
        }
        return "success:" + ret;
    }
}
