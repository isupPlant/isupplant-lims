package com.supcon.mes.module_lims.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.NetWorkUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.SupPlantApplication;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
public class WebSocketUtils {
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;

    private static Request request = null;
    public static WebSocketUtils instance;
    private boolean wantToRunning = true;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private int reconnectConsecutiveFailed = 0;

    private OkHttpClient.Builder builder;
    private OkHttpClient client;
    private WebSocket webSocket;
    private MyWebSocketListener listener = new MyWebSocketListener();
    private WebSocketCallBack mWebSocketCallBack;

    private WebSocketUtils() {
        builder = new OkHttpClient.Builder();
        client = builder
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }


    public static WebSocketUtils getInstance() {
        if (null == instance) {
            synchronized (StatusBarUtils.class) {
                if (null == instance) {
                    instance = new WebSocketUtils();
                }
            }
        }
        return instance;
    }

    public String url;

    public void setConnect(String url, WebSocketCallBack webSocketCallBack) {
        this.url = url;
        this.mWebSocketCallBack = webSocketCallBack;
        request = new Request.Builder().url("ws://" + url).build();
        webSocket = client.newWebSocket(request, listener);
        status = ConnectStatus.Connecting;
    }


    /**
     * 丢弃所有已经在队列中的消息然后残忍地关闭socket。
     */
    public void cancel() {
        if (webSocket != null) {
            status = ConnectStatus.DisConnect;
            webSocket.cancel();
        }
    }

    /**
     * 请求服务器优雅地关闭连接然后等待确认。在关闭之前，所有已经在队列中的消息将被传送完毕。
     */
    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

    /**
     * 1。如果有网络就重连；如果已经连续重连失败三次了也不能重连了，然后把重连次数重置为0
     * 2。如果没有网络就return
     */
    public void reconnect() {
        if (!NetWorkUtil.isNetConnected(SupPlantApplication.getAppContext())) {
            return;
        }
        if (webSocket != null)
            webSocket = client.newWebSocket(request, listener);
    }

    private ConnectStatus status;

    public ConnectStatus getStatus() {
        return status;
    }

    int reConnectCount = 0;

    private final class MyWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            status = ConnectStatus.Open;
            reConnectCount = 0;
            Log.i("WebSocketListener", "onOpen");
            if (null != mWebSocketCallBack) {
                mWebSocketCallBack.onOpen(webSocket, response);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            if (null != mWebSocketCallBack)
                mWebSocketCallBack.onMessage(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            LogUtil.e("Closing : " + code + " / " + reason);
            status = ConnectStatus.Closed;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            LogUtil.e("Error : " + t.getMessage());
            if (status == ConnectStatus.DisConnect)
                return;
            status = ConnectStatus.Canceled;
            String disConnectUrl = webSocket.request().url().host() + ":" + webSocket.request().url().port();
            if (status == ConnectStatus.Canceled && !TextUtils.isEmpty(url) && url.equals(disConnectUrl) && reConnectCount < 3) {
                reconnect();
                reConnectCount++;
            }
        }
    }

    public interface WebSocketCallBack {
        void onOpen(WebSocket webSocket, Response response);

        void onMessage(String text);
    }
}
