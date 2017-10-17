package org.wso2.extension.siddhi.io.websocket.sink;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/abc")
public class WebsocketEndpoint {

    @OnMessage
    public void onMessage(Session session, String message) {
        for (Session s : session.getOpenSessions()) {
            s.getAsyncRemote().sendText(message);
        }
    }
}
