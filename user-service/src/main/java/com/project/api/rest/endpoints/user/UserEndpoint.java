package com.project.api.rest.endpoints.user;

import com.project.api.resource.user.UserResource;
import com.project.api.resource.user.model.UserResourceDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.user.respond.UserPageResource;
import com.project.service.user.dto.UserDto;
import com.project.service.user.dto.UserFilterDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.mapper.UserMapper;
import com.project.service_impl.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserEndpoint implements UserResource {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserMapper userMapper;
    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntityWrapper<UserResourceDto> getByID(String id) {
        UserDto userDto = userMapper.userToUserDto(userService.getById(id));
        UserResourceDto userResourceDto = userMapper.userDtoToUserResourceDto(userDto);
        return ResponseEntityWrapper.<UserResourceDto>builder().message("User found").data(userResourceDto).build();
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntityWrapper<UserResourceDto> update(String id, UserUpdateRequestDto userUpdateRequestDto) {
        userService.update(id, userMapper.userUpdateRequestToUserUpdateDto(userUpdateRequestDto));
        return ResponseEntityWrapper.<UserResourceDto>builder().message("User updated").build();
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntityWrapper<UserResourceDto> delete(String id) {
        userService.deleteById(id);
        return ResponseEntityWrapper.<UserResourceDto>builder().message("User deleted").build();
    }

    @Override
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntityWrapper<UserPageResource> list(String id, String username, String email, int page, int size) {
        UserFilterDto userFilterDto = new UserFilterDto(email,  username, page, size);
        UserPageDto userPageDto = userService.list(userFilterDto);
        UserPageResource userPageResource = userMapper.userPageDtoToUserPageResource(userPageDto);
        return ResponseEntityWrapper.<UserPageResource>builder().message("User list").data(userPageResource).build();
    }
}
