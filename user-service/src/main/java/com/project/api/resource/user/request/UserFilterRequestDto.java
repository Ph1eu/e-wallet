package com.project.api.resource.user.request;

import com.project.api.common.model.PageRequestDto;
import jakarta.ws.rs.QueryParam;


public class UserFilterRequestDto extends PageRequestDto {
    @QueryParam("email")
    public String email = null;
    @QueryParam("username")
    public String username = null;

}
