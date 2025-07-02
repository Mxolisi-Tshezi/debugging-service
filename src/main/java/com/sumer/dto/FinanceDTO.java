package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FinanceDTO {
    private Long id;
    private String accountHolder;
    private String accountNumber;
    private String bank;
    private String branchCode;
    private Object sortCode;
    private String accountType;
}
