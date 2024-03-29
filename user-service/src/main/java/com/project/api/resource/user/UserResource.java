package com.project.api.resource.user;

import com.project.api.resource.user.model.UserResourceDto;
import com.project.api.resource.user.request.UserUpdateRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.user.respond.UserPageResource;
import jakarta.ws.rs.QueryParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
public interface UserResource {
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> getByID(@PathVariable String id);
    @PutMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> update(@PathVariable String id, @RequestBody  UserUpdateRequestDto userUpdateRequestDto);

    @DeleteMapping("/{id}")
    ResponseEntityWrapper<UserResourceDto> delete(@PathVariable String id);
    @GetMapping("/{id}")
    ResponseEntityWrapper<UserPageResource> list(@PathVariable String id, @QueryParam("username") String username, @QueryParam("email") String email, @QueryParam("page") int page, @QueryParam("size") int size);

}
