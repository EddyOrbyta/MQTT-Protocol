package it.orbyta.asyncmethod.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import org.eclipse.paho.mqttv5.common.MqttMessage;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Request extends MqttMessage {

    private String id;
    private String typeRequest;

}
