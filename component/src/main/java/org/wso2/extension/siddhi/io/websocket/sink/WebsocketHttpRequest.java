package org.wso2.extension.siddhi.io.websocket.sink;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by sivaramya on 9/15/17.
 */
public class WebsocketHttpRequest implements HttpRequest {
    @Override
    public HttpMethod getMethod() {
        return null;
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public HttpRequest setMethod(HttpMethod method) {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public HttpRequest setUri(String uri) {
        return null;
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return null;
    }

    @Override
    public HttpVersion protocolVersion() {
        return null;
    }

    @Override
    public HttpRequest setProtocolVersion(HttpVersion version) {
        return null;
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public DecoderResult getDecoderResult() {
        return null;
    }

    @Override
    public DecoderResult decoderResult() {
        return null;
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {

    }
}
