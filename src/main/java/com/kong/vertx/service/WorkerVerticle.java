/*
 * @(#)ApiWebApplication.java, 2022/4/19.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

/**
 * @package: com.kong.vertx.service
 * @className: LogService
 * @author: konglingfei
 * @date: 2022/4/19 18:34
 * @weekday: 星期二
 * @description:
 */
public class WorkerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        // 通过worker线程来执行
        // config 可以通过context上下文来获得
        JsonObject config = new JsonObject().put("name", "tim").put("directory", "/blah");
        DeploymentOptions options = new DeploymentOptions()
                .setInstances(8)
                .setWorker(true)
                .setConfig(config);
        vertx.deployVerticle("com.kong.vertx.service.TimerVerticle", options);

    }
}
