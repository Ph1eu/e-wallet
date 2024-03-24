package com.project.api.resource.user;

import com.project.api.resource.user.model.UserResourceDto;
import com.project.api.resource.user.request.UserCreateRequestDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import jakarta.ws.rs.QueryParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
public interface UserResource {
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> getByID(@PathVariable String id);
    @PutMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> update(@PathVariable String id, UserUpdateRequestDto userUpdateRequestDto);
    @PostMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> create(@PathVariable String id, UserCreateRequestDto userCreateRequestDto);
    @DeleteMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> delete(@PathVariable String id);
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> list(@PathVariable String id, @QueryParam("username") String username, @QueryParam("email") String email, @QueryParam("page") int page, @QueryParam("size") int size);

}
