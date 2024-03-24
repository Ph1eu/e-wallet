package com.project.service.user.mapper;

import com.project.api.resource.auth.request.RegisterRequestDto;
import com.project.api.resource.user.model.UserResourceDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.api.resource.user.respond.UserPageResource;
import com.project.service.address.mapper.AddressMapper;
import com.project.service.balanceinformation.mapper.BalanceInformationMapper;
import com.project.service.paymentcard.mapper.PaymentCardMapper;
import com.project.service.role.mapper.RoleMapper;
import com.project.service.user.dto.UserDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.dto.UserUpdateDto;
import com.project.service.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class, PaymentCardMapper.class, BalanceInformationMapper.class, RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source="address",target = "address",qualifiedByName = "addressToAddressDto")
    @Mapping(source="paymentCard",target = "paymentCard",qualifiedByName = "paymentCardToPaymentCardDto")
    @Mapping(source="balanceInformation",target = "balanceInformation",qualifiedByName = "balanceInformationToBalanceInformationDto")
    @Mapping(source = "role", target = "role", qualifiedByName = "eRoleToRoleString")
    UserDto userToUserDto(User user);
    @Mapping(source="address",target = "address",qualifiedByName = "addressDtoToAddress")
    @Mapping(source="paymentCard",target = "paymentCard",qualifiedByName = "paymentCardDtoToPaymentCard")
    @Mapping(source="balanceInformation",target = "balanceInformation",qualifiedByName = "balanceInformationDtoToBalanceInformation")
    @Mapping(source = "role", target = "role", qualifiedByName = "roleStringToERole")
    User userDtoToUser(UserDto userDto);

    UserDto RegisterRequestToUserDto(RegisterRequestDto registerRequestDto);
    UserResourceDto userDtoToUserResourceDto(UserDto userDto);
    UserUpdateDto userUpdateRequestToUserUpdateDto(UserUpdateRequestDto userUpdateRequestDto);
    UserPageResource userPageDtoToUserPageResource(UserPageDto userPageDto);
}