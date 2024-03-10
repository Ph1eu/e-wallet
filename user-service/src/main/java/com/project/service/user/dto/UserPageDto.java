package com.project.service.user.dto;

import com.project.service.user.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserPageDto {
    private int page;
    private int size;
    private List<User> content;
}
