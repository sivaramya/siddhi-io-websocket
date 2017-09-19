/*
package org.wso2.extension.siddhi.io.websocket.source;

import org.apache.log4j.Logger;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;
import org.wso2.carbon.transport.http.netty.common.Constants;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketClientConnector;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketConnectorListener;
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
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by sivaramya on 9/19/17.
 *//*


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
    private HttpWsConnectorFactoryImpl httpConnectorFactory = null;
    private SourceEventListener sourceEventListener;
    private Session session = null;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings,
                     ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.uri = optionHolder.validateAndGetStaticValue(URI);
        this.sourceEventListener = sourceEventListener;
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{String.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        Map<String, Object> senderProperties = new HashMap<>();
        senderProperties.put(Constants.REMOTE_ADDRESS, uri);
        senderProperties.put(Constants.WEBSOCKET_SUBPROTOCOLS, null);
        httpConnectorFactory = new HttpWsConnectorFactoryImpl();
        clientConnector = httpConnectorFactory.createWsClientConnector(senderProperties);
        WebSocketClientConnectorListener connectorListener = new WebSocketClientConnectorListener();
        try {
            session = handshake(connectorListener);
        } catch (ClientConnectorException e) {
            throw new ConnectionUnavailableException("Session was not available when trying to publish events " +
                    "in " + sourceEventListener);
        }
        String message = connectorListener.getReceivedTextToClient();
        sourceEventListener.onEvent(message, null);
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

    private Session handshake(WebSocketConnectorListener connectorListener) throws ClientConnectorException {
        Map<String, String> customHeaders = new HashMap<>();
        return clientConnector.connect(connectorListener, customHeaders);
    }
}
*/
