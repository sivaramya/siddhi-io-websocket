package org.wso2.extension.siddhi.io.websocket.sink;

import org.apache.log4j.Logger;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeFuture;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeListener;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketClientConnector;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketConnectorListener;
import org.wso2.extension.siddhi.io.websocket.util.WebSocketClientConnectorListener;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by sivaramya on 9/19/17.
 */
public class WebsocketPublisher {
    private static final Logger log = Logger.getLogger(WebsocketPublisher.class);
    private static WebSocketClientConnector clientConnector;

    public static void websocketPublish(WebSocketClientConnectorListener connectorListener, String message,
                                        WebSocketClientConnector clientConnector1) {
        clientConnector = clientConnector1;
        HandshakeFuture handshakeFuture = handshake(connectorListener);
        handshakeFuture.setHandshakeListener(new HandshakeListener() {
            @Override
            public void onSuccess(Session session) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("error in sending the message");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("error in sending the message");
            }
        });
    }

    private static HandshakeFuture handshake(WebSocketConnectorListener connectorListener) {
        return clientConnector.connect(connectorListener);
    }
}
