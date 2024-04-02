package it.orbyta.demo.service.util;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Callable;

@Slf4j
public class ProcessMessageTask implements Callable<Void> {
    private final MqttMessage mqttMessage;
    private final SimpMessagingTemplate messagingTemplate;

    public ProcessMessageTask(MqttMessage mqttMessage, SimpMessagingTemplate messagingTemplate) {
        this.mqttMessage = mqttMessage;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Void call() throws Exception {
        try {
            log.info("Start  message processing: {}", new String(mqttMessage.getPayload()));
            // Simulating message processing delay
            Thread.sleep(5000);
            // Convert MQTT message payload to String
            String messagePayload = new String(mqttMessage.getPayload()); // this is the message sent by MQTT
            // Send message to WebSocket clients
            sendMessageToClients(messagePayload);
            log.info("Process completed, SENDING MESSAGE =>  {}", messagePayload);
        } catch (InterruptedException e) {
            log.error("Error occurred during message processing: {}", e.getMessage());
        }
        return null;
    }

    private void sendMessageToClients(String message) {
        // Send message to destination "/queue/notification"
        messagingTemplate.convertAndSend("/topic", message);
        log.info("Preparing notification to clients: {}", message);
    }
}
