package com.project.api.rest.endpoints.user;

import com.project.api.resource.user.UserResource;
import com.project.api.resource.user.model.UserResourceDto;
import com.project.api.resource.user.request.UserCreateRequestDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import com.project.service.user.dto.UserDto;
import com.project.service.user.mapper.UserMapper;
import com.project.service_impl.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntityWrapper<UserResourceDto> getByID(String id) {
        UserDto userDto = userMapper.userToUserDto(userService.getById(id));
        UserResourceDto userResourceDto = userMapper.userDtoToUserResourceDto(userDto);
        return ResponseEntityWrapper.<UserResourceDto>builder().message("User found").data(userResourceDto).build();
    }

    @Override
    public ResponseEntityWrapper<UserResourceDto> update(String id, UserUpdateRequestDto userUpdateRequestDto) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<UserResourceDto> create(String id, UserCreateRequestDto userCreateRequestDto) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<UserResourceDto> delete(String id) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<UserResourceDto> list(String id, String username, String email, int page, int size) {
        return null;
    }
}
