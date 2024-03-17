package com.project.service.user.mapper;

import com.project.service.address.mapper.AddressMapper;
import com.project.service.balanceinformation.mapper.BalanceInformationMapper;
import com.project.service.paymentcard.mapper.PaymentCardMapper;
import com.project.service.user.dto.UserDto;
import com.project.service.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, PaymentCardMapper.class, BalanceInformationMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source="address",target = "address",qualifiedByName = "addressToAddressDto")
    @Mapping(source="paymentCard",target = "paymentCard",qualifiedByName = "paymentCardToPaymentCardDto")
    @Mapping(source="balanceInformation",target = "balanceInformation",qualifiedByName = "balanceInformationToBalanceInformationDto")
    UserDto userToUserDto(User user);
    @Mapping(source="address",target = "address",qualifiedByName = "addressDtoToAddress")
    @Mapping(source="paymentCard",target = "paymentCard",qualifiedByName = "paymentCardDtoToPaymentCard")
    @Mapping(source="balanceInformation",target = "balanceInformation",qualifiedByName = "balanceInformationDtoToBalanceInformation")
    User userDtoToUser(UserDto userDto);
}