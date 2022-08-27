/*
 * @(#)ApiWebApplication.java, 2022/4/19.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.service.schedule;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

/**
 * @package: com.kong.vertx.service
 * @className: TimerService
 * @author: konglingfei
 * @date: 2022/4/19 20:32
 * @weekday: 星期二
 * @description:
 */
public class TimerVerticle extends AbstractVerticle {


    /**
     * 继承了AbstractVerticle，会有很多方法可以进行重写，对应着开始，停止，销毁
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        // 对于每一个 Vert.x 实例来说它是单例的
        EventBus eb = vertx.eventBus();
        DeliveryOptions options = new DeliveryOptions();
        options.addHeader("some-header", "some-value");
        options.setSendTimeout(5000);


        HttpClientOptions httpOptions = new HttpClientOptions()
                .setDefaultHost("127.0.0.1")
                .setDefaultPort(8000);
        HttpClient client = vertx.createHttpClient(httpOptions);

        vertx.setPeriodic(60000, id -> {
            // This handler will get called every second
            // 这个处理器将会每隔一秒被调用一次
            eb.send("news.uk.sport", "hello world!", options);

            // 每五秒发送一个请求
            client.request(HttpMethod.GET, "/some-uri", response -> {
                System.out.println("Received response with status code " + response.succeeded());
            });
        });
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
