package com.sumer.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestResult {

    @JsonProperty("paymentRequestId")
    private String paymentRequestId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("errorMessage")
    private String errorMessage;
}

