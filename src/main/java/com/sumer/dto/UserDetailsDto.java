package com.sumer.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String title;
    private String email;
    private List<AuthorityDto> authorityList;
    private List<AddressDTO> addressList;
    private InsuranceConsentDto insuranceConsent;
}
