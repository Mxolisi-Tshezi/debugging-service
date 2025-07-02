package com.sumer.mapper;

import com.sumer.dto.*;
import com.sumer.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    public UserDto mapUserToDtoBasic(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setName(user.getName());
        return userDto;
    }

    public AddressDTO mapAddressToDtoBasic(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setName(address.getName());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setProvince(address.getProvince());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setPhoneNumber(address.getPhoneNumber());
        return addressDTO;
    }


    public CategoryDto mapCategoryToDtoBasic(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public OrderItemDto mapOrderItemToDtoBasic(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setStatus(orderItem.getStatus().name());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());
        return orderItemDto;
    }

    public ProductDto mapProductToDtoBasic(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        return productDto;
    }

    public UserDto mapUserToDtoPlusAddress(User user) {
        UserDto userDto = mapUserToDtoBasic(user);
        if (user.getAddress() != null) {
            AddressDTO addressDto = mapAddressToDtoBasic(user.getAddress());
            userDto.setAddress(addressDto);
        }
        return userDto;
    }

    public OrderItemDto mapOrderItemToDtoPlusProduct(OrderItem orderItem) {
        OrderItemDto orderItemDto = mapOrderItemToDtoBasic(orderItem);
        if (orderItem.getProduct() != null) {
            ProductDto productDto = mapProductToDtoBasic(orderItem.getProduct());
            orderItemDto.setProduct(productDto);
        }
        return orderItemDto;
    }

    public OrderItemDto mapOrderItemToDtoPlusProductAndUser(OrderItem orderItem) {
        OrderItemDto orderItemDto = mapOrderItemToDtoBasic(orderItem);
        if (orderItem.getUser() != null) {
            UserDto userDto = mapUserToDtoPlusAddress(orderItem.getUser());
            orderItemDto.setUser(userDto);
        }
        return orderItemDto;
    }

    public UserDto mapUserToDtoAddressAndOrderHistory(User user) {
        UserDto userDto = mapUserToDtoPlusAddress(user);
        if (user.getOrderItemList() != null && !user.getOrderItemList().isEmpty()) {
            userDto.setOrderItemList(user.getOrderItemList()
                    .stream()
                    .map(this::mapOrderItemToDtoPlusProduct)
                    .collect(Collectors.toList()));
        }
        return userDto;
    }

    public Insurance mapToInsuranceEntity(InsuranceDTO insuranceDTO) {
        Insurance insurance = new Insurance();

        Set<Policy> policies = insuranceDTO.getPolicies() != null
                ? insuranceDTO.getPolicies().stream()
                .map(this::mapToPolicyEntity)
                .collect(Collectors.toSet())
                : new HashSet<>();

        insurance.setPolicies(policies);

        List<Address> addresses = insuranceDTO.getAddresses() != null
                ? insuranceDTO.getAddresses().stream()
                .map(this::mapToAddressEntity)
                .collect(Collectors.toList())
                : new ArrayList<>();
        insurance.setAddresses(addresses);

        Set<CustomerDetailField> detailFields = insuranceDTO.getDetailFields() != null
                ? insuranceDTO.getDetailFields().stream()
                .map(this::mapToCustomerDetailFieldEntity)
                .collect(Collectors.toSet())
                : new HashSet<>();
        insurance.setDetailFields(detailFields);


        insurance.setPremium(insuranceDTO.getPremium());
        insurance.setCommission(insuranceDTO.getCommission());

        return insurance;
    }



    public Policy mapToPolicyEntity(PolicyDTO policyDTO) {
        Policy policy = new Policy();
        policy.setHeader(policyDTO.getHeader());
        policy.setFinance(policyDTO.getFinance());
        policy.setRisks(policyDTO.getRisks());
        policy.setDetailFields(policyDTO.getDetailFields());
        return policy;
    }

    public PolicyHeader mapToHeaderEntity(PolicyHeaderDTO headerDTO) {
        PolicyHeader policyHeader = new PolicyHeader();
        policyHeader.setProductId(headerDTO.getProductId());
        policyHeader.setSubCampaignId(headerDTO.getSubCampaignId());
        policyHeader.setCommissionVersion(headerDTO.getCommissionVersion());
        policyHeader.setOldPolicyNumber(headerDTO.getOldPolicyNumber());
        policyHeader.setActualPolicyNumber(headerDTO.getActualPolicyNumber());
        policyHeader.setComment(headerDTO.getComment());
        policyHeader.setCountryAlpha2Code(headerDTO.getCountryAlpha2Code());
        policyHeader.setCoverStartDate(headerDTO.getCoverStartDate());
        return policyHeader;
    }

    public Address mapToAddressEntity(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setName(addressDTO.getName());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setProvince(addressDTO.getProvince());
        address.setZipCode(addressDTO.getZipCode());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        return address;
    }


    public DetailField mapToDetailFieldEntity(DetailFieldDTO detailFieldDTO) {
        DetailField detailField = new DetailField();
        detailField.setCode(detailFieldDTO.getCode());
        detailField.setValue(detailFieldDTO.getValue());
        detailField.setDescription(detailFieldDTO.getDescription());
        return detailField;
    }

    public ArrayList<CoInsurer> mapToCoInsurerEntities(List<CoInsurerDTO> coInsurerDTOs) {
        return (ArrayList<CoInsurer>) coInsurerDTOs.stream().map(this::mapToCoInsurerEntity).collect(Collectors.toList());
    }

    public CoInsurer mapToCoInsurerEntity(CoInsurerDTO coInsurerDTO) {
        CoInsurer coInsurer = new CoInsurer();
        coInsurer.setPercentage(coInsurerDTO.getPercentage());
        coInsurer.setCode(coInsurerDTO.getCode());
        return coInsurer;
    }

    public CustomerDetailField mapToCustomerDetailFieldEntity(CustomerDetailFieldDTO customerDetailFieldDTO) {
        CustomerDetailField customerDetailField = new CustomerDetailField();
        customerDetailField.setCode(customerDetailFieldDTO.getCode());
        customerDetailField.setValue(customerDetailFieldDTO.getValue());
        return customerDetailField;
    }

}
