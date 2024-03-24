package com.project.service.user;

import com.project.service.user.dto.UserDto;
import com.project.service.user.dto.UserFilterDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.dto.UserUpdateDto;
import com.project.service.user.entity.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(String id);
    User getById(String id);
    UserPageDto list(UserFilterDto filter);
    void deleteById(String id);
    User create(UserDto user);
    void update(String userId,UserUpdateDto userUpdateDto);
    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User getByUsername(String username);

}
