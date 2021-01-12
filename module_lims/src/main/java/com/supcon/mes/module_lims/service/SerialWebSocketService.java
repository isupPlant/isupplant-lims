package com.supcon.mes.module_lims.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.controller.NetworkChangeController;
import com.supcon.mes.middleware.model.event.NetworkChangeEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.controller.SerialDeviceController;
import com.supcon.mes.module_lims.utils.WebSocketUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.Response;
import okhttp3.WebSocket;


/**
 * author huodongsheng
 * on 2021/1/12
 * class name
 */
public class SerialWebSocketService extends Service {
    public static String START_SERIAL_SERVICE = "startSerialService";
    public static String STOP_SERIAL_SERVICE = "stopSerialService";
    NetworkChangeController changeController;
    WebSocketUtils webSocketUtils;
    String url = null, openSendMsg = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            stopSelf();
            return START_STICKY;
        }

        url = intent.getStringExtra("url");
        openSendMsg = intent.getStringExtra("openSendMsg");
        if (intent.getAction().equals(START_SERIAL_SERVICE)){
            changeController = new NetworkChangeController(this);
            changeController.onStart();

            startConnect();
        }else if (intent.getAction().equals(STOP_SERIAL_SERVICE)){
            stopConnect();
            stopForeground(true);
            stopSelf();

            if (changeController != null) {
                changeController = null;
            }
        }



        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startConnect() {
        if (null == webSocketUtils){
            webSocketUtils = WebSocketUtils.getInstance();
            webSocketUtils.setConnect(url, new WebSocketUtils.WebSocketCallBack() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    webSocket.send(openSendMsg);
                }

                @Override
                public void onMessage(String text) {
                    String msg = text;
                }
            });
        }

    }

    private void stopConnect(){
        if (webSocketUtils != null){
            webSocketUtils.stop();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetworkChangeEvent event) {
        LogUtil.e("service has receive the network change message");
        if (webSocketUtils != null) {
            webSocketUtils.reconnect();
        }
    }
}
