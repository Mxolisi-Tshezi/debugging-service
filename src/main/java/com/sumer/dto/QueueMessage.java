package com.sumer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueueMessage {
    private Long insuranceId;
    private String action;
    private String status;
}

