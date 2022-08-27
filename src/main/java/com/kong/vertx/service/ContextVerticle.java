/*
 * @(#)ApiWebApplication.java, 2022/4/28.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;

/**
 * @package: com.kong.vertx.service
 * @className: ContextVerticle
 * @author: konglingfei
 * @date: 2022/4/28 14:44
 * @weekday: 星期四
 * @description:
 */
public class ContextVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Context context = vertx.getOrCreateContext();
        if (context.isEventLoopContext()) {
            System.out.println("Context attached to Event Loop");
        } else if (context.isWorkerContext()) {
            System.out.println("Context attached to Worker Thread");
        } else if (!Context.isOnVertxThread()) {
            System.out.println("Context not attached to a thread managed by vert.x");
        }
    }
}
