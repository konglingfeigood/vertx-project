package com.kong.vertx.service.kafka;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.io.File;
import java.util.Map;

/**
 * @package: com.kong.vertx.service
 * @className: MainVerticle
 * @author: konglingfei
 * @date: 2022/4/19 18:34
 * @weekday: 星期二
 * @description:
 */
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  private KafkaCluster kafkaCluster;

  @Override
  public void start() throws Exception {

    // Kafka setup for the example
    File dataDir = Testing.Files.createTestingDirectory("cluster");
    dataDir.deleteOnExit();
    kafkaCluster = new KafkaCluster()
      .usingDirectory(dataDir)
      .withPorts(2181, 9092)
      .addBrokers(1)
      .deleteDataPriorToStartup(true)
      .startup();

    // Deploy the dashboard
    JsonObject consumerConfig = new JsonObject((Map) kafkaCluster.useTo()
      .getConsumerProperties("the_group", "the_client", OffsetResetStrategy.LATEST));
    vertx.deployVerticle(
      DashboardVerticle.class.getName(),
      new DeploymentOptions().setConfig(consumerConfig)
    );

    // Deploy the metrics collector : 3 times
    for (int i = 0;i < 3;i++) {
      JsonObject producerConfig = new JsonObject((Map) kafkaCluster.useTo()
        .getProducerProperties("the_producer-" + i));
      vertx.deployVerticle(
        MetricsVerticle.class.getName(),
        new DeploymentOptions().setConfig(producerConfig)
      );
    }
  }

  @Override
  public void stop() throws Exception {
    kafkaCluster.shutdown();
  }
}