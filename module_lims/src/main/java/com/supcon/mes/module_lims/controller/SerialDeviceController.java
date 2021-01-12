package com.supcon.mes.module_lims.controller;

import android.content.Context;
import android.view.View;

import com.supcon.common.view.base.controller.BaseController;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.utils.WebSocketUtils;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;


/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
public class SerialDeviceController extends BaseController {
//    private String serialPort; //串口号
//    private String baudRate; //波特率
//    private String checkDigit; //校验位
//    private String dataBits; //数据位
//    private String stopBits; //停止位
Context context;

    public SerialDeviceController(Context context) {
        this.context = context;
    }


    public void connectWebSocket(String serverIp,String openSendMsg){
        WebSocketUtils instance = WebSocketUtils.getInstance();
        instance.setConnect("ws://" + serverIp, new WebSocketUtils.WebSocketCallBack() {
            @Override
            public void onOpen(WebSocket webSocket,Response response) {
                webSocket.send(openSendMsg);
            }

            @Override
            public void onMessage(String text) {

            }

        });
    }
}
