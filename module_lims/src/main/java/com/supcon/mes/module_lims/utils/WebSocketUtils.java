package com.supcon.mes.module_lims.utils;

import android.content.Context;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.NetWorkUtil;
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
    private static WebSocketUtils instance;
    private boolean wantToRunning = true;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private int reconnectConsecutiveFailed = 0;

    private OkHttpClient.Builder builder;
    private OkHttpClient client;
    private WebSocket webSocket;
    private MyWebSocketListener listener;
    private WebSocketCallBack mWebSocketCallBack;

    private WebSocketUtils() {
    }


    public static WebSocketUtils getInstance() {
       if (null == instance){
           return new WebSocketUtils();
       }
        return instance;
    }

    public void setConnect(String url,WebSocketCallBack webSocketCallBack){
        this.mWebSocketCallBack = webSocketCallBack;
        init(url);
    }


    private void init(String url) {
        builder = new OkHttpClient.Builder();
        if (client == null) {
            synchronized (WebSocketUtils.class) {
                if (client == null)
                    client = builder
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                            .build();
            }
        }

        if (request == null) {
            request = new Request.Builder().get().url("ws://"+url).build();
        }
        listener = new MyWebSocketListener();
    }

    /**
     * 开始
     */
    public void start() {
        wantToRunning = true;

        //把原来的都取消掉
        if (client != null) {
            client.dispatcher().cancelAll();
        }

        if (client == null) {
            client = builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).build();
        }

        try {
            webSocket = client.newWebSocket(request, listener);
        } catch (Exception e) {

        }
    }


    /**
     * 停止
     */
    public void stop() {
        wantToRunning = false;
        if (client != null) {
            client.dispatcher().cancelAll();
        }

        webSocket = null;
    }

    /**
     * 1。如果有网络就重连；如果已经连续重连失败三次了也不能重连了，然后把重连次数重置为0
     * 2。如果没有网络就return
     */
    public void reconnect() {
        if (webSocket == null) {
            start();
            return;
        }

        if (!NetWorkUtil.isNetConnected(SupPlantApplication.getAppContext())) {
            return;
        }

        if (reconnectConsecutiveFailed == 3) {
            reconnectConsecutiveFailed = 0;
            return;
        }

        reconnectConsecutiveFailed++;

        client.dispatcher().cancelAll();
        webSocket = client.newWebSocket(request, listener);
    }

    private final class MyWebSocketListener extends WebSocketListener{
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            if (null != mWebSocketCallBack)
                mWebSocketCallBack.onOpen(webSocket,response);
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
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            if (wantToRunning){
                reconnect();
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            LogUtil.e("Error : " + t.getMessage());
            if (wantToRunning){
                reconnect();
            }
        }
    }

    public interface WebSocketCallBack{
        void onOpen(WebSocket webSocket,Response response);
        void onMessage( String text);
    }
}
