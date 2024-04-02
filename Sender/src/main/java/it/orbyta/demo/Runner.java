package it.orbyta.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.test", havingValue = "false")
@AllArgsConstructor
public class Runner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String topic = "MQTT with websockets";
        String content = "Message from Spring Boot via MQTT!";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setConnectionTimeout(10); // Set connection timeout to 10 seconds
            connOpts.setAutomaticReconnect(true);
            connOpts.setCleanStart(false);

            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            log.info("Connecting to broker: {}", broker);

            IMqttToken token = sampleClient.connect(connOpts);
            token.waitForCompletion();
            log.info("Connected");

            log.info("Publishing message: {}", content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            token = sampleClient.publish(topic, message);
            token.waitForCompletion();

            log.info("Message published");
            log.info("Disconnecting");
            sampleClient.disconnect().waitForCompletion();
            log.info("Disconnected");

            log.info("Closing client");
            sampleClient.close();
            log.info("Client closed");
        } catch (MqttException me) {
            log.error("MQTT Exception: reason {}, msg {}, loc {}, cause {}, excep {}",
                    me.getReasonCode(), me.getMessage(), me.getLocalizedMessage(),
                    me.getCause(), me.getStackTrace());
        }
    }
}

