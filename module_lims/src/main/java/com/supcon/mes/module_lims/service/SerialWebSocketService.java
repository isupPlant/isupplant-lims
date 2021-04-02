package com.supcon.mes.module_lims.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.controller.NetworkChangeController;
import com.supcon.mes.middleware.model.event.NetworkChangeEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.module_lims.utils.ConnectStatus;
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
            if (changeController==null){
                changeController = new NetworkChangeController(this);
                changeController.onStart();
            }
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
        if (null == webSocketUtils) {
            webSocketUtils = WebSocketUtils.getInstance();
        }
        if (!TextUtils.isEmpty(webSocketUtils.url) && webSocketUtils.url.equals(url) && webSocketUtils.getStatus()== ConnectStatus.Open){
            handler.sendEmptyMessage(100);
            return;
        }

        if (webSocketUtils.getStatus()==ConnectStatus.Open){
            webSocketUtils.cancel();
        }
        webSocketUtils.setConnect(url, new WebSocketUtils.WebSocketCallBack() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                handler.sendEmptyMessage(100);
            }

            @Override
            public void onMessage(String text) {
                if (!TextUtils.isEmpty(text)) {
                    SelectDataEvent dataEvent = new SelectDataEvent(text, "WebSocketData");
                    EventBus.getDefault().post(dataEvent);
                }
            }
        });
        webSocketUtils.setOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                handler.sendEmptyMessage(-200);
            }
        });

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==100){
                ToastUtils.show(SupPlantApplication.getAppContext(),"设备连接成功！");
            }else if (msg.what==-200){
                ToastUtils.show(SupPlantApplication.getAppContext(),"设备已断开连接！");
            }
        }
    };


    private void stopConnect(){
        if (webSocketUtils != null){
            webSocketUtils.close();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetworkChangeEvent event) {
        LogUtil.e("service has receive the network change message");
        if (webSocketUtils != null ) {
            webSocketUtils.reconnect();
        }
    }
}
