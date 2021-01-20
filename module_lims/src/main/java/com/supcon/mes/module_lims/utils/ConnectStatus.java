package com.supcon.mes.module_lims.utils;

/**
 * Created by wanghaidong on 2021/1/19
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public enum ConnectStatus {
    Connecting, // the initial state of each web socket.
    Open, // the web socket has been accepted by the remote peer
    Closing, // one of the peers on the web socket has initiated a graceful shutdown
    Closed, //  the web socket has transmitted all of its messages and has received all messages from the peer
    Canceled, // the web socket connection failed
    DisConnect //主动断开
}
