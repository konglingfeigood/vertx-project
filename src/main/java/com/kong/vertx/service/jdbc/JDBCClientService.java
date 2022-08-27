package com.kong.vertx.service.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * @package: com.kong.vertx.service
 * @className: JDBCClientService
 * @author: konglingfei
 * @date: 2022/4/19 20:28
 * @weekday: 星期二
 * @description:
 */
public class JDBCClientService extends AbstractVerticle{
    //将客户端对象与Verticle对象绑定，这里选取了三种不同的客户端作为示范
    HttpClient httpClient;
    JDBCClient jdbcClient;


    @Override
    public void start(){
          //创建客户端
        httpClient = vertx.createHttpClient();
        JsonObject config = new JsonObject()
                .put("jdbcUrl", "...")
                .put("maximumPoolSize", 30)
                .put("username", "db user name")
                .put("password", "***")
                .put("provider_class", "...");

        jdbcClient = JDBCClient.createShared(vertx, config);

          //using clients
    }
}