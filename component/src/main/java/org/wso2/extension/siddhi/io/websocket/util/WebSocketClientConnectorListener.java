package org.wso2.extension.siddhi.io.websocket.util;

import org.apache.log4j.Logger;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketBinaryMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketCloseMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketConnectorListener;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketControlMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketControlSignal;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketInitMessage;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketTextMessage;

import java.nio.ByteBuffer;

/**
 * Created by sivaramya on 9/19/17.
 */
public class WebSocketClientConnectorListener implements WebSocketConnectorListener {
    private static final Logger log = Logger.getLogger(WebSocketConnectorListener.class);

    private String receivedTextToClient;
    private ByteBuffer receivedByteBufferToClient;
    private boolean isPongReceived = false;

    @Override
    public void onMessage(WebSocketInitMessage initMessage) {
    }

    @Override
    public void onMessage(WebSocketTextMessage textMessage) {
        receivedTextToClient = textMessage.getText();
    }

    @Override
    public void onMessage(WebSocketBinaryMessage binaryMessage) {
        receivedByteBufferToClient = binaryMessage.getByteBuffer();
    }

    @Override
    public void onMessage(WebSocketControlMessage controlMessage) {
        if (controlMessage.getControlSignal() == WebSocketControlSignal.PONG) {
            isPongReceived = true;
        }
    }

    @Override
    public void onMessage(WebSocketCloseMessage closeMessage) {

    }

    @Override
    public void onError(Throwable throwable) {
        handleError(throwable);
    }

    /**
     * Retrieve the latest text received to client.
     *
     * @return the latest text received to the client.
     */
    public String getReceivedTextToClient() {
        String tmp = receivedTextToClient;
        receivedTextToClient = null;
        return tmp;
    }

    /**
     * Retrieve the latest {@link ByteBuffer} received to client.
     *
     * @return the latest {@link ByteBuffer} received to client.
     */
    public ByteBuffer getReceivedByteBufferToClient() {
        ByteBuffer tmp = receivedByteBufferToClient;
        receivedByteBufferToClient = null;
        return tmp;
    }

    /**
     * Retrieve whether a pong is received client.
     *
     * @return true if a pong is received to client.
     */
    public boolean isPongReceived() {
        boolean tmp = isPongReceived;
        isPongReceived = false;
        return tmp;
    }

    private void handleError(Throwable throwable) {
        log.error(throwable.getMessage());
    }

}
