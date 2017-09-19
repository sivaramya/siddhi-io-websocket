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
import org.wso2.carbon.messaging.Header;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;

import java.util.ArrayList;
import java.util.List;


/**
 * {@code HttpSinkUtil } responsible of the all configuration reading and input formatting of http transport.
 */
public class WebsocketSinkUtil {
    private static final Logger log = Logger.getLogger(WebsocketSinkUtil.class);
    public static final String HEADER_SPLITTER = "','";
    public static final String HEADER_NAME_VALUE_SPLITTER = ":";

    public WebsocketSinkUtil() {
    }

    public List<Header> getHeaders(String headers) {
        headers = headers.trim();
        headers = headers.substring(1, headers.length() - 1);
        List<Header> headersList = new ArrayList<>();
        if (!"".equals(headers)) {
            String[] spam = headers.split(HEADER_SPLITTER);
            for (String aSpam : spam) {
                String[] header = aSpam.split(HEADER_NAME_VALUE_SPLITTER, 2);
                if (header.length > 1) {
                    headersList.add(new Header(header[0], header[1]));
                } else {
                    throw new SiddhiAppRuntimeException(
                            "Invalid header format. Please include as 'key1:value1','key2:value2',..");
                }
            }
        }
        return headersList;
    }
}
