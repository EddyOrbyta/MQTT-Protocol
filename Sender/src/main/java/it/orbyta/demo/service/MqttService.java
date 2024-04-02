package it.orbyta.demo.service;

import it.orbyta.demo.dto.Request;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqttService {
    private final static String BROKER = "tcp://localhost:1883";
    private final static String CLIENT_ID = "JavaSample";

    public boolean sendMessage(Request request) throws MqttException {
        log.info("Received request: {} with content => {}", request.getTopic() ,request.getContent());
        MemoryPersistence persistence = new MemoryPersistence();

        MqttConnectionOptions connOpts = new MqttConnectionOptions();
        connOpts.setConnectionTimeout(10); // Set connection timeout to 10 seconds
        connOpts.setAutomaticReconnect(true); // Enable automatic reconnection
        connOpts.setCleanStart(false); // Set clean start to false
        log.info("Connecting to broker: {}", BROKER);
        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(BROKER, CLIENT_ID, persistence);
            IMqttToken token = sampleClient.connect(connOpts);
            log.info("Connected");
            token.waitForCompletion();
            log.info("Publishing message: {}", request.getContent());
            MqttMessage message = new MqttMessage(request.getContent().getBytes());
            message.setQos(request.getQos());

            for (int i = 0; i <2; i++) { // Example: sending 10 messages

                sampleClient.publish(request.getTopic(), message)
                        .waitForCompletion();
                log.info("Message n. {} published ", i);
            }

            log.info("Disconnecting");
            sampleClient.disconnect().waitForCompletion();
            log.info("Disconnected");
            log.info("Closing client");
            sampleClient.close();
            log.info("Client closed");
            return  true;

        }catch (Exception e) {
            log.error("Error sending message {} Stacktrace => {}", e.getMessage(),  e.getStackTrace());
            return false;
        }
    }
}
