package it.orbyta.asyncmethod.service;

import it.orbyta.asyncmethod.entity.Request;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@Slf4j
public class RequestLookupService {

    private final IMqttClient publisher;

    public RequestLookupService(IMqttClient publisher) {
        this.publisher = publisher;
    }

    @Async
    public CompletableFuture<Request> executeRequest(Request request) {
        log.info("Processing request: {}", request);
        try {
            Thread.sleep(2000); // Simulate processing time
        } catch (InterruptedException e) {
            log.error("Thread interrupted while processing request", e);
            Thread.currentThread().interrupt();
        }
        log.info("Completed processing request: {}", request);

        // Start MQTT message generation
        RequestMessageGenerator messageGenerator = new RequestMessageGenerator(publisher, request);
        Supplier<Void> supplier = () -> {
            try {
                messageGenerator.call();
            } catch (Exception e) {
                // Gestione dell'eccezione, se necessario
            }
            return null;
        };
        return CompletableFuture.completedFuture(request);
    }
}

