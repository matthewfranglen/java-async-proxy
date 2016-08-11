package com.matthew.async.controller;

import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProxyController {

    @RequestMapping("/**")
    public Future<String> handle(HttpServletRequest request) {

    }

}
