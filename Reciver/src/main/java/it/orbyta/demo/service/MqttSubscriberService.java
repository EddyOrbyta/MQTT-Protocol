package it.orbyta.demo.service;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqttSubscriberService {
    private final MqttSubscriberComponent mqttSubscriberComponent;

    public MqttSubscriberService(MqttSubscriberComponent mqttSubscriberComponent) {
        this.mqttSubscriberComponent = mqttSubscriberComponent;
    }

    //@Bean add this comment for starting the always alive service that start a connection with mqtt
    // and also a subscription to all topics with qos 2
    // In addition, this method is responsible for sending a message via websocket  once subscribed
    public void start() {
        try {
            log.info("Subscribing => START");
            mqttSubscriberComponent.subscribe("#", 2);
        } catch (MqttException e) {
            log.error("Error subscribing {}", e.getMessage());
        }
    }

    public void stop() {
        try {
            log.info("Subscribing => END");
            mqttSubscriberComponent.disconnect();
        } catch (MqttException e) {
            log.error("Error subscribing {}", e.getMessage());
        }
    }
}
