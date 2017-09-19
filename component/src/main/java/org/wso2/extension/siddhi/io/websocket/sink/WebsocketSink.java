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
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.carbon.messaging.Header;
import org.wso2.carbon.messaging.exceptions.ClientConnectorException;
import org.wso2.carbon.transport.http.netty.common.Constants;
import org.wso2.carbon.transport.http.netty.config.ListenerConfiguration;
import org.wso2.carbon.transport.http.netty.internal.websocket.WebSocketSessionImpl;
import org.wso2.carbon.transport.http.netty.listener.WebSocketSourceHandler;
import org.wso2.carbon.transport.http.netty.sender.websocket.WebSocketClientConnector;
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
import java.net.URISyntaxException;
import java.util.List;
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
    private static final String HEADERS = "headers";
    private static final String CLIENT_SERVICE_NAME = "client.service.name";
    private static final String SUBPROTOCLOS = "subprotocols";
    private static final String ALLOW_EXTENSIONS = "allow.extensions";
    private static final String CHANNEL_ID = "channel.id";
    private static final String SECURE_ENABLED = "secure.enabled";

    private StreamDefinition streamDefinition;
    private Session session = null;
    private String uri;
    private String clientServiceName;
    private WebSocketClientConnector webSocketClientConnector = null;
    private String subprotocols;
    private boolean allowExtensions;
    private WebSocketSourceHandler sourceHandler = null;
    private String header;
    private String channelId;
    private boolean isSecured;


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
        this.clientServiceName = optionHolder.validateAndGetStaticValue(CLIENT_SERVICE_NAME);
        this.subprotocols = optionHolder.validateAndGetStaticValue(SUBPROTOCLOS);
        this.allowExtensions = Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(ALLOW_EXTENSIONS));
        this.header = optionHolder.validateAndGetStaticValue(HEADERS);
        this.channelId = optionHolder.validateAndGetStaticValue(CHANNEL_ID);
        this.isSecured = Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(SECURE_ENABLED));

    }

    @Override
    public void connect() throws ConnectionUnavailableException {
        List<Header> headers = null;
        headers = new WebsocketSinkUtil().getHeaders(header);
        webSocketClientConnector = new WebSocketClientConnector();
        WebsocketChannelHandlerContext channelHandlerContext = new WebsocketChannelHandlerContext();
        ListenerConfiguration listenerConfiguration = new ListenerConfiguration();
        WebsocketHttpRequest httpRequestUtil = new WebsocketHttpRequest();
        String sessionId = "websocket";
        try {
            WebSocketSessionImpl serverSession = new WebSocketSessionImpl(channelHandlerContext , isSecured, uri,
                    sessionId);
            sourceHandler = new WebSocketSourceHandler(channelId, null, listenerConfiguration,
                    httpRequestUtil, isSecured, channelHandlerContext, serverSession);
        } catch (URISyntaxException e) {
            throw new ConnectionUnavailableException("Error in connecting with the websocket client" +
                    streamDefinition);
        } catch (Exception e) {
            throw new ConnectionUnavailableException("Error in connecting with the websocket client" +
                    streamDefinition);
        }
        CarbonMessage cMessage = new DefaultCarbonMessage();
        cMessage.setProperty(Constants.REMOTE_ADDRESS, uri);
        cMessage.setProperty(Constants.TO, clientServiceName);
        cMessage.setProperty("WEBSOCKET_SUBPROTOCOLS", subprotocols);
        cMessage.setProperty("WEBSOCKET_ALLOW_EXTENSIONS", allowExtensions);
        cMessage.setProperty(Constants.SRC_HANDLER, sourceHandler);
        cMessage.setHeaders(headers);
        try {
            session = (Session) webSocketClientConnector.init(cMessage, null, null);
        } catch (ClientConnectorException e) {
            throw new ConnectionUnavailableException("Error in connecting with the websocket client" +
                    streamDefinition);
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

}
