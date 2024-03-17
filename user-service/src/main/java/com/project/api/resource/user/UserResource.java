package com.project.api.resource.user;

import com.project.api.resource.user.model.UserDto;
import com.project.api.resource.user.request.UserCreateRequestDto;
import com.project.api.resource.user.request.UserFilterRequestDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.payload.response.ResponseEntityWrapper;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
public interface UserResource {
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserDto> getByID( @PathVariable String id);
    @PutMapping("/{id}")
    ResponseEntityWrapper<UserDto> update(@PathVariable String id, UserUpdateRequestDto userUpdateRequestDto);
    @PostMapping("/{id}")
    ResponseEntityWrapper<UserDto> create(@PathVariable String id, UserCreateRequestDto userCreateRequestDto);
    @DeleteMapping("/{id}")
    ResponseEntityWrapper<UserDto> delete(@PathVariable String id);
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserDto> list(@PathVariable String id, @QueryParam("username") String username, @QueryParam("email") String email, @QueryParam("page") int page, @QueryParam("size") int size);

}
