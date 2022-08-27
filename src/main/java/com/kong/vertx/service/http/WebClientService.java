/*
 * @(#)ApiWebApplication.java, 2022/4/19.
 *
 *  Copyright 2022 Youdao, Inc. All rights reserved.
 *  YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.kong.vertx.service.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.client.WebClient;

/**
 * @package: com.kong.vertx.service
 * @className: WebClientService
 * @author: konglingfei
 * @date: 2022/4/19 20:28
 * @weekday: 星期二
 * @description:
 */
public class WebClientService extends AbstractVerticle {

    private HttpClient httpClient;

    private WebClient webClient;

    @Override
    public void start() throws Exception {
          //创建客户端
        httpClient = vertx.createHttpClient();
        webClient = WebClient.wrap(httpClient);

    }
}
