package org.wso2.extension.siddhi.io.websocket.source;

import org.apache.log4j.Logger;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;
import org.wso2.carbon.transport.http.netty.common.Constants;
import org.wso2.carbon.transport.http.netty.contract.websocket.*;
import org.wso2.carbon.transport.http.netty.contractimpl.HttpWsConnectorFactoryImpl;
import org.wso2.extension.siddhi.io.websocket.sink.WebsocketSink;
import org.wso2.extension.siddhi.io.websocket.util.WebSocketClientConnectorListener;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code WebsocketSink } Handle the websocket publishing tasks.
 */

@Extension(
        name = "websocket",
        namespace = "source",
        description = "description ",
        examples = @Example(description = "TBD", syntax = "TBD")
)

public class WebsocketSource extends Source {
    private static final Logger log = Logger.getLogger(WebsocketSink.class);

    private static final String URI = "uri";

    private String uri;
    private WebSocketClientConnector clientConnector;
    private SourceEventListener sourceEventListener;
    private WebSocketClientConnectorListener connectorListener;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings,
                     ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.uri = optionHolder.validateAndGetStaticValue(URI);
        this.sourceEventListener = sourceEventListener;
        connectorListener = new WebSocketClientConnectorListener();
        HttpWsConnectorFactoryImpl httpConnectorFactory = new HttpWsConnectorFactoryImpl();
        WsClientConnectorConfig configuration = new WsClientConnectorConfig(uri);
        clientConnector = httpConnectorFactory.createWsClientConnector(configuration);
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{String.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        HandshakeFuture handshakeFuture = handshake(connectorListener);
        handshakeFuture.setHandshakeListener(new HandshakeListener() {
            @Override
            public void onSuccess(Session session) {
                connectorListener.setSourceEventListener(sourceEventListener);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("error in sending the message");
            }
        });
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

    private HandshakeFuture handshake(WebSocketConnectorListener connectorListener) {
        return clientConnector.connect(connectorListener);
    }
}
