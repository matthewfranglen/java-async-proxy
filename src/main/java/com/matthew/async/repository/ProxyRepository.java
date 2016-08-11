package com.matthew.async.repository;

import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

@Repository
public class ProxyRepository {

    public Future<String> request(HttpServletRequest request) {

    }

}
