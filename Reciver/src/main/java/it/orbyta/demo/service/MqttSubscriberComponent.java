package it.orbyta.demo.service;

import it.orbyta.demo.service.util.MqttCallbackConf;
import it.orbyta.demo.service.util.ProcessMessageTask;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class MqttSubscriberComponent {

    private final MqttAsyncClient mqttClient;
    private final BlockingQueue<MqttMessage> messageQueue;
    private final SimpMessagingTemplate messagingTemplate;
    private static final String BROKER = "tcp://localhost:1883";

    private static final String CLIENT_ID = "orbyta-client";

    public MqttSubscriberComponent(@Value("${mqtt.brokerUrl:" + BROKER + "}") String brokerUrl,
                                   @Value("${mqtt.clientId:" + CLIENT_ID+ "}") String clientId,
                                   SimpMessagingTemplate messagingTemplate) throws MqttException {
        this.messagingTemplate = messagingTemplate;
        mqttClient = new MqttAsyncClient(brokerUrl, clientId, new MemoryPersistence()); // Create the MQTT client
        MqttConnectionOptions connOpts = new MqttConnectionOptions(); // Connection options
        connOpts.setCleanStart(true); // Clean session
        mqttClient.connect(connOpts).waitForCompletion(); // Connect and wait
        messageQueue = new LinkedBlockingQueue<>(); // Create a queue to store the messages
        mqttClient.setCallback(new MqttCallbackConf(messageQueue)); // Set the callback
    }

    public void subscribe(String topic, int qos) throws MqttException {
        IMqttToken token = mqttClient.subscribe(topic, qos);
        token.waitForCompletion();
        if (token.isComplete() && token.getException() == null) {
            log.info("Successfully subscribed to topic: {}", topic);
            startMessageProcessor(); // this is the function that runs in a separate thread and
            // processes the messages received via MQTT protocol
        } else {
            log.error("Failed to subscribe to topic: {}", topic);
        }
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    private void startMessageProcessor() {
        while (true) {
            try {
                MqttMessage message = messageQueue.take(); // Wait for new messages
                log.info("Message received and ready to be processed: {}. Current queue size: {}",
                        new String(message.getPayload()), messageQueue.size());
                // Task to be executed
                ProcessMessageTask task = new ProcessMessageTask(message, messagingTemplate);
                // Run the task
              task.call(); // Execute the task directly
            } catch (InterruptedException e) {
                log.error("Error processing message: {}", e.getMessage());
            } catch (Exception e) {
               log.error("Error occurred while starting a new task for processing a message: {}", e.getMessage());
            }
        }
    }

}
