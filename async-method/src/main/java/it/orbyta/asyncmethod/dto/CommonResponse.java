package it.orbyta.asyncmethod.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommonResponse {

    public String id;
    public String msg;
}
