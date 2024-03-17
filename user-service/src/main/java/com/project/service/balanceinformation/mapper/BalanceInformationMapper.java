package com.project.service.balanceinformation.mapper;

import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.balanceinformation.entity.BalanceInformation;
import org.mapstruct.factory.Mappers;

public interface BalanceInformationMapper {
    BalanceInformationMapper INSTANCE = Mappers.getMapper(BalanceInformationMapper.class);
    BalanceInformationDto balanceInformationToBalanceInformationDto(BalanceInformation balanceInformation);
    BalanceInformation balanceInformationDtoToBalanceInformation(BalanceInformationDto balanceInformationDto);
}
