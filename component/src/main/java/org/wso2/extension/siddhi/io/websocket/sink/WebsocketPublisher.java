package org.wso2.extension.siddhi.io.websocket.sink;

import org.apache.log4j.Logger;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeFuture;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeListener;
import java.io.IOException;
import javax.websocket.Session;

/**
 * Created by sivaramya on 9/19/17.
 */
public class WebsocketPublisher {
    private static final Logger log = Logger.getLogger(WebsocketPublisher.class);

    public static void websocketPublish(HandshakeFuture handshakeFuture, String message) {
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
                log.error("error while connecting with the server");
            }
        });
    }


}
