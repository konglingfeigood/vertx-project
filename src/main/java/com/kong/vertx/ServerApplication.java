/*
 * @(#)ApiWebApplication.java, 2022/4/19.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx;

import com.kong.vertx.eventbus.EventBusConsumerVerticle;
import com.kong.vertx.service.FuturePromiseVerticle;
import com.kong.vertx.service.http.HttpServerVerticle;
import com.kong.vertx.service.WorkerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @package: com.kong.vertx
 * @className: HttpServer
 * @author: konglingfei
 * @date: 2022/4/19 17:38
 * @weekday: 星期二
 * @description:
 */
public class ServerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ServerApplication.class);



    public static void main(String[] args) {

        // Vertx vertx = Vertx.vertx();
        // 可以设置不同的配置，包括集群、高可用、线程池大小等
        // 在这个初始化Vertx的时候，构造函数中，同时初始化了一个EventLoop，worker，还有一个acceptorEventLoopGroup
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(2));

        // 两种方式：
        // 第一种就是创建一个类WcService，重写start方法，也是官方推荐用法
        vertx.deployVerticle(HttpServerVerticle.class.getName());

        // 如果想执行某个任务，可以在启动的时候就执行
        vertx.deployVerticle(WorkerVerticle.class.getName());

        vertx.deployVerticle(EventBusConsumerVerticle.class.getName());

        vertx.deployVerticle(FuturePromiseVerticle.class.getName());

        // 第二种就是直接在启动的main方法里面初始化一个HttpServer;
//        HttpServer server = vertx.createHttpServer();
//        server.requestHandler(request -> {
//            // This handler gets called for each request that arrives on the server
//            HttpServerResponse response = request.response();
//            response.putHeader("content-type", "text/plain");
//
//            // Write to the response and end it
//            response.end("Hello World!");
//        });
//
//        server.listen(8080);
    }


}
