package com.matthew.async.repository;

import java.net.URI;
import java.util.Enumeration;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxInvocationBuilder;
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
        RxInvocationBuilder<RxCompletionStageInvoker> builder = target.path(request.getRequestURI()).request();
        builder = setHeaders(request, builder);
        return builder.rx().get();
    }

    private RxInvocationBuilder<RxCompletionStageInvoker> setHeaders(HttpServletRequest request, RxInvocationBuilder<RxCompletionStageInvoker> builder) {
        return reduce(request.getHeaderNames(), builder, applyHeader(request));
    }

    private BiFunction<RxInvocationBuilder<RxCompletionStageInvoker>, String, RxInvocationBuilder<RxCompletionStageInvoker>> applyHeader(HttpServletRequest request) {
        return (RxInvocationBuilder<RxCompletionStageInvoker> builder, String header) -> reduce(request.getHeaders(header), builder, applyHeader(header));
    }

    private BiFunction<RxInvocationBuilder<RxCompletionStageInvoker>, String, RxInvocationBuilder<RxCompletionStageInvoker>> applyHeader(String header) {
        return (RxInvocationBuilder<RxCompletionStageInvoker> builder, String value) -> builder.header(header, value);
    }

    private <T, V> T reduce(Enumeration<V> values, T initial, BiFunction<T, V, T> reducer) {
        T current = initial;

        while (values.hasMoreElements()) {
            current = reducer.apply(current, values.nextElement());
        }

        return current;
    }

}
