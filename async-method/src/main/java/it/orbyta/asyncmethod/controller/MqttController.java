package it.orbyta.asyncmethod.controller;

import it.orbyta.asyncmethod.dto.CommonResponse;
import it.orbyta.asyncmethod.entity.Request;
import it.orbyta.asyncmethod.service.RequestLookupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mqtt")
@AllArgsConstructor
public class MqttController {

    private final RequestLookupService service;

    @PostMapping("/publish")
    public ResponseEntity<CommonResponse> publish(@RequestBody Request request) {
        CompletableFuture<Request> future = service.executeRequest(request);
        future.join(); // Attendere il completamento di executeRequest

        return ResponseEntity.ok(CommonResponse.builder().id(String.valueOf(request.getId())).msg("Request published").build());
    }

}

