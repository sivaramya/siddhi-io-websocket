package org.wso2.extension.siddhi.io.websocket.source;

import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeFuture;
import org.wso2.carbon.transport.http.netty.contract.websocket.HandshakeListener;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketClientConnector;
import org.wso2.carbon.transport.http.netty.contract.websocket.WsClientConnectorConfig;
import org.wso2.carbon.transport.http.netty.contractimpl.HttpWsConnectorFactoryImpl;
import org.wso2.extension.siddhi.io.websocket.util.WebSocketClientConnectorListener;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;

import java.nio.ByteBuffer;
import java.util.Map;
import javax.websocket.Session;

/**
 * {@code WebsocketSource } Handle the websocket receiving tasks.
 */

@Extension(
        name = "websocket",
        namespace = "source",
        description = "description ",
        examples = @Example(description = "TBD", syntax = "TBD")
)

public class WebsocketSource extends Source {
    private static final String URI = "uri";

    private WebSocketClientConnector clientConnector;
    private SourceEventListener sourceEventListener;
    private WebSocketClientConnectorListener connectorListener;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings,
                     ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        String uri = optionHolder.validateAndGetStaticValue(URI);
        this.sourceEventListener = sourceEventListener;
        connectorListener = new WebSocketClientConnectorListener();
        HttpWsConnectorFactoryImpl httpConnectorFactory = new HttpWsConnectorFactoryImpl();
        WsClientConnectorConfig configuration = new WsClientConnectorConfig(uri);
        clientConnector = httpConnectorFactory.createWsClientConnector(configuration);
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{String.class, ByteBuffer.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        HandshakeFuture handshakeFuture = clientConnector.connect(connectorListener);
        handshakeFuture.setHandshakeListener(new HandshakeListener() {
            @Override
            public void onSuccess(Session session) {
                connectorListener.setSourceEventListener(sourceEventListener);
            }

            @Override
            public void onError(Throwable throwable) {
                throw new SiddhiAppRuntimeException("Error while connecting with the websocket server defined in "
                                                            + sourceEventListener);
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
}
