package com.project.service.balanceinformation.mapper;

import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.balanceinformation.entity.BalanceInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BalanceInformationMapper {
    BalanceInformationMapper INSTANCE = Mappers.getMapper(BalanceInformationMapper.class);
    @Named("balanceInformationToBalanceInformationDto")
    BalanceInformationDto balanceInformationToBalanceInformationDto(BalanceInformation balanceInformation);
    @Named("balanceInformationDtoToBalanceInformation")
    BalanceInformation balanceInformationDtoToBalanceInformation(BalanceInformationDto balanceInformationDto);
}
