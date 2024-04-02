package it.orbyta.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {

    private String topic;
    private String content;
    private int qos;

}
