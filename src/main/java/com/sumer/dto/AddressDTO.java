package com.sumer.dto;

import com.sumer.entity.Insurance;
import com.sumer.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long id;
    private String name;
    private String street;
    private String city;
    private String province;
    private String zipCode;
    private String phoneNumber;
    private User user;
    private Insurance insurance;
}
