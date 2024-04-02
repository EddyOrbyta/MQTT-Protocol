package it.orbyta.demo.service.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;

@Configuration
@NoArgsConstructor
@Slf4j
public class MqttCallbackConf implements MqttCallback {

    private BlockingQueue<MqttMessage> messageQueue;
    public MqttCallbackConf(BlockingQueue<MqttMessage> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {

    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Add the incoming message to the queue
        log.info("PUT IN QUEUE message =>  {} on topic: {}", new String(message.getPayload()), topic);
        messageQueue.put(message);
    }

    @Override
    public void deliveryComplete(IMqttToken token) {

    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {

    }
}
