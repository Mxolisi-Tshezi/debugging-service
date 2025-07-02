package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sumer.entity.InsuranceConsent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private String token;
    private String role;
    private String expirationTime;
    private QueueMessage queueMessage;

    private int totalPage;
    private long totalElement;

    private Object data;

    private Double quotePrice;

    private AddressDTO address;
    private UserDto user;
    private List<UserDto> userList;

    private CategoryDto category;
    private List<CategoryDto> categoryList;

    private ProductDto product;
    private List<ProductDto> productList;

    private OrderItemDto orderItem;
    private List<OrderItemDto> orderItemList;

    private OrderDto order;
    private List<OrderDto> orderList;
    private InsuranceDTO insuranceData;
    private InsuranceConsent insuranceConsent;
}
