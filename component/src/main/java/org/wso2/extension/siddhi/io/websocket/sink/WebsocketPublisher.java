package org.wso2.extension.siddhi.io.websocket.sink;

import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeFuture;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeListener;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.io.IOException;
import javax.websocket.Session;

/**
 * {@code WebsocketPublisher } Handle the websocket publishing tasks.
 */

public class WebsocketPublisher {

    public static void websocketPublish(HandshakeFuture handshakeFuture, String message,
                                        StreamDefinition streamDefinition) {
        handshakeFuture.setHandshakeListener(new HandshakeListener() {
            @Override
            public void onSuccess(Session session) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    throw new SiddhiAppRuntimeException("Error in sending the message to the websocket server defined"
                                                                + " in " + streamDefinition);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throw new SiddhiAppRuntimeException("Error while connecting with the websocket server defined in "
                                                            + streamDefinition);
            }
        });
    }


}
