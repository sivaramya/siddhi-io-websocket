/*
 *  Copyright (c) 2017 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.extension.siddhi.io.websocket.sink;

import org.apache.log4j.Logger;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;
import org.wso2.carbon.transport.http.netty.common.Constants;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketClientConnector;
import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketConnectorListener;
import org.wso2.carbon.transport.http.netty.contractimpl.HttpWsConnectorFactoryImpl;;
import org.wso2.extension.siddhi.io.websocket.util.WebSocketClientConnectorListener;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.Session;

/**
 * {@code WebsocketSink } Handle the websocket publishing tasks.
 */

@Extension(
        name = "websocket",
        namespace = "sink",
        description = "description ",
        examples = @Example(description = "TBD", syntax = "TBD")
)

public class WebsocketSink extends Sink {
    private static final Logger log = Logger.getLogger(WebsocketSink.class);

    private static final String URI = "uri";

    private StreamDefinition streamDefinition;
    private Session session = null;
    private String uri;
    private WebSocketClientConnector clientConnector;
    private HttpWsConnectorFactoryImpl httpConnectorFactory = null;

    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[]{String.class};
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder,
                        ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.streamDefinition = streamDefinition;
        this.uri = optionHolder.validateAndGetStaticValue(URI);
        httpConnectorFactory = new HttpWsConnectorFactoryImpl();

    }

    @Override
    public void connect() throws ConnectionUnavailableException {
        Map<String, Object> senderProperties = new HashMap<>();
        senderProperties.put(Constants.REMOTE_ADDRESS, uri);
        senderProperties.put(Constants.WEBSOCKET_SUBPROTOCOLS, null);
        clientConnector = httpConnectorFactory.createWsClientConnector(senderProperties);
        WebSocketClientConnectorListener connectorListener = new WebSocketClientConnectorListener();
        try {
            session = handshake(connectorListener);
        } catch (ClientConnectorException e) {
            throw new ConnectionUnavailableException("Session was not available when trying to publish events " +
                    "in " + streamDefinition);
        }
    }

    @Override
    public void publish(Object payload, DynamicOptions dynamicOptions) throws ConnectionUnavailableException {
        if (session == null) {
            throw new ConnectionUnavailableException("Session was not available when trying to publish events " +
                    "in " + streamDefinition);
        } else if (!session.isOpen()) {
            throw new ConnectionUnavailableException("Session was not open when trying " +
                    "to publish events in  " + streamDefinition +
                    ", for  Session ID: " + session.getId());
        } else {
            String message = (String) payload;
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new SiddhiAppRuntimeException("Error in publishing the message via websocket in " +
                        streamDefinition);
            }
        }
    }


    @Override
    public void disconnect() {
        try {
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error in disconnection the session");
            }
        }
    }

    @Override
    public void destroy() {
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
