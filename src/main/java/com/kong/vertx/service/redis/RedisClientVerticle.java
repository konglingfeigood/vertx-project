package com.kong.vertx.service.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.util.Arrays;

/**
 * @package: com.kong.vertx.service
 * @className: RedisClientVerticle
 * @author: konglingfei
 * @date: 2022/5/19 18:34
 * @weekday: 星期二
 * @description:
 */
public class RedisClientVerticle extends AbstractVerticle {

  @Override
  public void start() {
    // If a config file is set, read the host and port.
    String host = Vertx.currentContext().config().getString("host");
    if (host == null) {
      host = "127.0.0.1";
    }

    // Create the redis client
    Redis client = Redis.createClient(vertx, new RedisOptions().addConnectionString(host));
    RedisAPI redis = RedisAPI.api(client);

    client.connect()
      .compose(conn ->
        redis.set(Arrays.asList("key", "value"))
          .compose(v -> {
            System.out.println("key stored");
            return redis.get("key");
          }))
      .onSuccess(result -> {
        System.out.println("Retrieved value: " + result);
      })
      .onFailure(err -> {
        System.out.println("Connection or Operation Failed " + err);
      });
  }
}