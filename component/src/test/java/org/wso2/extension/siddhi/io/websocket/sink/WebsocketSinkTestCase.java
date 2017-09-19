/*
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.io.websocket.sink;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;;
import org.wso2.carbon.messaging.ClientConnector;
import org.wso2.carbon.transport.http.netty.listener.HTTPServerConnector;
import org.wso2.carbon.transport.http.netty.sender.websocket.WebSocketClientConnector;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WebsocketSinkTestCase {
    private static final Logger log = Logger.getLogger(WebsocketSinkTestCase.class);
    private AtomicInteger eventCount = new AtomicInteger(0);
    private List<String> receivedEventNameList;
    private int waitTime = 50;
    private int timeout = 30000;
    ClientConnector clientConnector = new WebSocketClientConnector();
    private List<HTTPServerConnector> serverConnectors;

    @BeforeMethod
    public void init() {
        eventCount.set(0);
    }

    @Test
    public void websocketSinkAndSourceTestCase() throws InterruptedException {
        log.info("---------------------------------------------------------------------------------------------");
        log.info("websocket sink and source test case");
        log.info("---------------------------------------------------------------------------------------------");
        receivedEventNameList = new ArrayList<>(3);
        SiddhiManager siddhiManager = new SiddhiManager();
        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(
                "@App:name('TestExecutionPlan') " +
                        "define stream FooStream1 (symbol string, price float, volume long); " +
                        "@info(name = 'query1') " +
                        "@sink(type='websocket', uri = 'ws://localhost:8025/websockets', " +
                        "client.service.name = 'websocket', allow.extensions = 'true', subprotocols = 'xml', " +
                        "headers = \"'A:B'\", secure.enabled = 'false', channel.id='websocket', " +
                        "@map(type='xml'))" +
                        "Define stream BarStream1 (symbol string, price float, volume long);" +
                        "from FooStream1 select symbol, price, volume insert into BarStream1;");
        InputHandler fooStream = executionPlanRuntime.getInputHandler("FooStream1");

        executionPlanRuntime.start();
        ArrayList<Event> arrayList = new ArrayList<Event>();
        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"WSO2", 55.6f, 100L}));
        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"IBM", 75.6f, 100L}));
        arrayList.add(new Event(System.currentTimeMillis(), new Object[]{"WSO2", 57.6f, 100L}));
        fooStream.send(arrayList.toArray(new Event[3]));
        List<String> expected = new ArrayList<>(2);
        expected.add("WSO2");
        expected.add("IBM");
        expected.add("WSO2");
        Thread.sleep(10000);
        executionPlanRuntime.shutdown();
    }
}
