package com.matthew.async.repository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.matthew.async.dto.RequestDetails;

@Repository
public class ProxyRepository {

    @Value("${proxy.host:localhost}")
    private String host;

    @Value("${proxy.port:80}")
    private int port;

    private RxWebTarget<RxCompletionStageInvoker> target;

    @PostConstruct
    public void init() {
        URI url = UriBuilder.fromPath("http://{host}:{port}").build(host, port);
        target = Rx.newClient(RxCompletionStageInvoker.class)
            .target(url);
    }

    public CompletionStage<Response> get(RequestDetails details) {
        RxWebTarget<RxCompletionStageInvoker> target = this.target.path(details.getPath());

        for (Map.Entry<String, List<String>> param : details.getQueryParameters().entrySet()) {
            List<String> value = param.getValue();
            target = target.queryParam(param.getKey(), (Object[])value.toArray());
        }

        RxInvocationBuilder<RxCompletionStageInvoker> builder = target.request();

        for (Map.Entry<String, List<String>> header : details.getHeaders().entrySet()) {
            for (String value : header.getValue()) {
                builder = builder.header(header.getKey(), value);
            }
        }

        return builder.rx().get();
    }

}
