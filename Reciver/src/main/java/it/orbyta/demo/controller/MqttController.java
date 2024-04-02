package it.orbyta.demo.controller;

import it.orbyta.demo.dto.CommonResponse;
import it.orbyta.demo.service.MqttSubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqtt")
@AllArgsConstructor
public class MqttController {

    private final MqttSubscriberService subService;

    @PostMapping("start")
    public ResponseEntity<CommonResponse> start() {
        subService.start();
        return ResponseEntity.ok(new CommonResponse("Subscriber started"));
    }
    @PostMapping("stop")
    public ResponseEntity<CommonResponse> stop() {
        subService.stop();
        return ResponseEntity.ok(new CommonResponse("Subscriber stopped"));
    }
 
}
