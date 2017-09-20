package org.wso2.extension.siddhi.io.websocket.util;

import org.apache.log4j.Logger;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketBinaryMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketCloseMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketConnectorListener;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketControlMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketControlSignal;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketInitMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketTextMessage;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;

import java.nio.ByteBuffer;

/**
 * Created by sivaramya on 9/19/17.
 */
public class WebSocketClientConnectorListener implements WebSocketConnectorListener {
    private static final Logger log = Logger.getLogger(WebSocketConnectorListener.class);

    private String receivedTextToClient;
    //private ByteBuffer receivedByteBufferToClient;
    private SourceEventListener sourceEventListener = null;

    public void setSourceEventListener (SourceEventListener eventListener) {
        sourceEventListener = eventListener;
    }

    @Override
    public void onMessage(WebSocketInitMessage initMessage) {
    }

    @Override
    public void onMessage(WebSocketTextMessage textMessage) {
        receivedTextToClient = textMessage.getText();
        sourceEventListener.onEvent(receivedTextToClient, null);
    }

    @Override
    public void onMessage(WebSocketBinaryMessage binaryMessage) {
        //receivedByteBufferToClient = binaryMessage.getByteBuffer();
    }

    @Override
    public void onMessage(WebSocketControlMessage controlMessage) {
    }

    @Override
    public void onMessage(WebSocketCloseMessage closeMessage) {

    }

    @Override
    public void onError(Throwable throwable) {
        handleError(throwable);
    }

    @Override
    public void onIdleTimeout(WebSocketControlMessage webSocketControlMessage) {
    }


    private void handleError(Throwable throwable) {
        log.error(throwable.getMessage());
    }

}