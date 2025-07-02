package com.sumer.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDto {

    private String email;
    private String phone;
    private String address;
    private String contactPerson;
    private String department;
}
