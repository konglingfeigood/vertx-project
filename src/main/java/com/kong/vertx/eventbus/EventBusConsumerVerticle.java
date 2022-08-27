/*
 * @(#)ApiWebApplication.java, 2022/4/24.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * @package: com.kong.vertx.service
 * @className: EventBusVerticle
 * @author: konglingfei
 * @date: 2022/4/24 19:36
 * @weekday: 星期日
 * @description:
 */
public class EventBusConsumerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // 对于每一个 Vert.x 实例来说它是单例的
        EventBus eb = vertx.eventBus();
        eb.consumer("news.uk.sport", message -> {
            System.out.println("I have received a message: " + message.body());
            message.reply("I have received your message!");
        });


//        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
//        consumer.completionHandler(res -> {
//            if (res.succeeded()) {
//                System.out.println("The handler registration has reached all nodes");
//                consumer.handler(msg ->{
//                    System.out.println("I have received a message: " + msg.body());
//                });
//            } else {
//                System.out.println("Registration failed!");
//            }
//        });
    }
}
