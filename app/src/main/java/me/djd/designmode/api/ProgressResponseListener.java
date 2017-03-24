package me.djd.designmode.api;

/**
 * Created by dujiande on 2017/3/22.
 * 响应体进度回调接口，用于文件下载进度回调
 */

public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
