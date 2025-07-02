package com.sumer.service.interf;


import com.sumer.dto.AddressDTO;
import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(Long id, AddressDTO addressDTO);

    void deleteAddress(Long id);

    AddressDTO getAddressById(Long id);

    List<AddressDTO> getAllAddresses();
}

