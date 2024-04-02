package it.orbyta.asyncmethod.service;

import it.orbyta.asyncmethod.entity.Request;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.Random;
import java.util.concurrent.Callable;

public class RequestMessageGenerator implements Callable<Void> {

    private static final String TOPIC = "request";
    private final IMqttClient client;
    private final Request request;
    private final Random random = new Random();

    public RequestMessageGenerator(IMqttClient client, Request request) {
        this.client = client;
        this.request = request;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Void call() throws Exception {
        if(!client.isConnected()){
            return null;
        }
        MqttMessage message = request;
        message.setQos(0);
        message.setRetained(true);
        client.publish(TOPIC, message);
        return null;
    }




}
