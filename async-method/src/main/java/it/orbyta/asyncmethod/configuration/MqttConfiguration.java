package it.orbyta.asyncmethod.configuration;

import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
public class MqttConfiguration {
    private final static String publisherId = UUID.randomUUID().toString();
    @Bean
    public IMqttClient publisher() throws MqttException {
        return new MqttClient("tcp://iot.eclipse.org:1883",publisherId);
    }
    @Bean
    public void mqttConnectOptions() throws MqttException {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setCleanStart(false);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        publisher().connect(options);
    }
}
