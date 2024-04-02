package it.orbyta.demo.controller;

import it.orbyta.demo.dto.CommonResponse;
import it.orbyta.demo.dto.Request;
import it.orbyta.demo.service.MqttService;
import lombok.AllArgsConstructor;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqtt")
@AllArgsConstructor
public class MqttController {

    private final MqttService service;

    @PostMapping("send")
    public ResponseEntity<CommonResponse> send(@RequestBody Request request) throws MqttException {
        if(!service.sendMessage(request)) {
            return ResponseEntity.badRequest().body(new CommonResponse("Message not sent"));
        }else {
            return ResponseEntity.ok(new CommonResponse("Message sent"));
        }
    }
 
}
