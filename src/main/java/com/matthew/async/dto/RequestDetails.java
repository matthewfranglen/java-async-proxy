package com.matthew.async.dto;

import org.springframework.util.MultiValueMap;

public class RequestDetails {

    private final MultiValueMap<String, String> headers;
    private final String path;
    private final MultiValueMap<String, String> queryParameters;
    private final String body;

    public RequestDetails(
            MultiValueMap<String, String> headers,
            String path,
            MultiValueMap<String, String> queryParameters,
            String body
    ) {
        this.headers = headers;
        this.path = path;
        this.queryParameters = queryParameters;
        this.body = body;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }

    public MultiValueMap<String, String> getQueryParameters() {
        return queryParameters;
    }

    public String getBody() {
        return body;
    }

}
