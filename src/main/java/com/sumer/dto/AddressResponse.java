package com.sumer.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AddressResponse {
    private int code;
    private String message;
    private AddressRequest data;
    private QueueMessage queueMessage;
}

