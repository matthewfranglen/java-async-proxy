package com.matthew.async.repository;

import java.net.URI;
import java.util.concurrent.CompletionStage;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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

    public CompletionStage<Response> get(HttpServletRequest request) {
        return target.request().rx().get();
    }

}
