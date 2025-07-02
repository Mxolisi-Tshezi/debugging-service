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
public class CustomerHeaderDTO {
    private String firstName;
    private String lastName;
    private String initials;
    private String customerType;
    private String companyName;
    private String contactName;
    private int customerId;
    private int commissionRelId;
    private String language;
    private int postalAddressId;
    private int physicalAddressId;
    private int workAddressId;
    private boolean itcCheckAuthorised;
    private String countryAlpha2Code;
    private boolean vatRegistered;
    private String vatRegNo;
}
