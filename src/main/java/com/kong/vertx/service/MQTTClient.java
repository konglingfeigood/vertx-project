package com.kong.vertx.service;

import com.kong.vertx.util.Runner;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/**
 * An example of using the MQTT client as a verticle
 */
public class MQTTClient extends AbstractVerticle {

  private static final String MQTT_TOPIC = "/my_topic";
  private static final String MQTT_MESSAGE = "Hello Vert.x MQTT Client";
  private static final String BROKER_HOST = "localhost";
  private static final int BROKER_PORT = 1883;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MQTTClient.class);
  }

  @Override
  public void start() throws Exception {
    MqttClient mqttClient = MqttClient.create(vertx);

    mqttClient.connect(BROKER_PORT, BROKER_HOST, ch -> {
      if (ch.succeeded()) {
        System.out.println("Connected to a server");

        mqttClient.publish(
          MQTT_TOPIC,
          Buffer.buffer(MQTT_MESSAGE),
          MqttQoS.AT_MOST_ONCE,
          false,
          false,
          s -> mqttClient.disconnect(d -> System.out.println("Disconnected from server")));
      } else {
        System.out.println("Failed to connect to a server");
        System.out.println(ch.cause());
      }
    });
  }
}