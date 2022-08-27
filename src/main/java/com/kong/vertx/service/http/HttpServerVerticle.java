/*
 * @(#)ApiWebApplication.java, 2022/4/20.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.service.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;

/**
 * @package: com.kong.vertx.service
 * @className: HttpServerVerticle
 * @author: konglingfei
 * @date: 2022/4/20 11:26
 * @weekday: 星期三
 * @description:
 */
public class HttpServerVerticle extends AbstractVerticle {

    int num = 1;

    @Override
    public void start() throws Exception {

        // 启动两个服务，分别监听不同的端口
        // createHttpClient()创建一个HTTP / HTTPS客户端
        // createHttpServer()创建一个HTTP / HTTPS服务器
        // createNetClient()创建了一个TCP / SSL客户端
        // createNetServer()创建了一个TCP / SSL服务器
        // creatSockJSServer()创建一个包装了HTTP服务器SockJS服务器
        // eventBus()提供对事件总线的应用程序访问
        // fileSystem()提供对文件系统的应用程序访问
        // sharedData()提供应用程序访问共享数据对象，它可以被用来共享Verticles之间的数据
        vertx.createHttpServer()
                .requestHandler(request -> {

                    String path = request.path();
                    System.out.println("path = " + path);

                    String query = request.query();
                    System.out.println("query =" + query);

                    String ip = request.remoteAddress().hostAddress();
                    System.out.println("ip =" + ip);

                    String absoluteURI = request.absoluteURI();
                    System.out.println("absoluteURI =" + absoluteURI);

                    HttpServerResponse response = request.response();
                    response.putHeader("content-type", "text/plain");
                    response.end("hello, I am a server!!!");
                })
                .listen(8000, http -> {
                    if (http.succeeded()) {
                        System.out.println("HTTP server started on port 8000");
                    } else {
                        System.out.println("fail start on port 8000");
                    }
                });

        vertx.createHttpServer()
                .requestHandler(request -> {
                    HttpServerResponse response = request.response();
                    response.putHeader("content-type", "text/plain");
                    response.end("hello, I am HttpServerVerticle, num = " + num);
                })
                .listen(8001, http -> {
                    if (http.succeeded()) {
                        System.out.println("HTTP server started on port 8001");
                    } else {
                        System.out.println("fail start on port 8001");
                    }
                });
    }
}