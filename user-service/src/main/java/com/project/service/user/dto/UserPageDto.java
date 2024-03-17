package com.project.service.user.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class UserPageDto {
    @NonNull
    private Integer page;
    @NonNull
    private Integer size;
    @NonNull
    private List<UserDto> content;
}
