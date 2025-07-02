package com.sumer.mapper;

import com.sumer.dto.RiskDtos;
import com.sumer.entity.Risk;

public class RiskMapper {

    // Convert DTO to Entity
    public Risk mapDtoToEntity(RiskDtos riskDtos) {
        Risk risk = new Risk();
        risk.setDeviceMake(riskDtos.getDeviceMake());
        risk.setDeviceModel(riskDtos.getDeviceModel());
        //risk.setIsDevicePrepaid(riskDtos.getIsDevicePrepaid());
        risk.setServiceProvider(riskDtos.getServiceProvider());
        risk.setAddressLine1(riskDtos.getAddressLine1());
        risk.setSumInsured(riskDtos.getSumInsured());
        risk.setDecline(riskDtos.getDecline());
        // Optionally map the header and detailFields if applicable
        return risk;
    }

    // Convert Entity to DTO
    public RiskDtos mapEntityToDto(Risk risk) {
        RiskDtos riskDtos = new RiskDtos();
        riskDtos.setDeviceMake(risk.getDeviceMake());
        riskDtos.setDeviceModel(risk.getDeviceModel());
        riskDtos.setIsDevicePrepaid(risk.isDevicePrepaid());
        riskDtos.setServiceProvider(risk.getServiceProvider());
        riskDtos.setAddressLine1(risk.getAddressLine1());
        riskDtos.setSumInsured(risk.getSumInsured());
        riskDtos.setDecline(risk.isDecline());
        // Optionally map the header and detailFields if applicable
        return riskDtos;
    }
}

