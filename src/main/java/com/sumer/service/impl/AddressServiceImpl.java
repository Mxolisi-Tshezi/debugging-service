package com.sumer.service.impl;

import com.sumer.service.interf.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.sumer.dto.AddressDTO;
import com.sumer.entity.Address;
import com.sumer.mapper.EntityDtoMapper;
import com.sumer.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, EntityDtoMapper entityDtoMapper) {
        this.addressRepository = addressRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = entityDtoMapper.mapToAddressEntity(addressDTO);
        Address savedAddress = addressRepository.save(address);
        return entityDtoMapper.mapAddressToDtoBasic(savedAddress);
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address addressToUpdate = optionalAddress.get();
            addressToUpdate.setName(addressDTO.getName());
            addressToUpdate.setStreet(addressDTO.getStreet());
            addressToUpdate.setCity(addressDTO.getCity());
            addressToUpdate.setProvince(addressDTO.getProvince());
            addressToUpdate.setZipCode(addressDTO.getZipCode());
            addressToUpdate.setPhoneNumber(addressDTO.getPhoneNumber());

            Address updatedAddress = addressRepository.save(addressToUpdate);
            return entityDtoMapper.mapAddressToDtoBasic(updatedAddress);
        } else {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
    }

    @Override
    public void deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            return entityDtoMapper.mapAddressToDtoBasic(optionalAddress.get());
        } else {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(entityDtoMapper::mapAddressToDtoBasic)
                .collect(Collectors.toList());
    }
}

