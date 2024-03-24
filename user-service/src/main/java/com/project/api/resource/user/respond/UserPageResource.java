package com.project.api.resource.user.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.resource.user.UserResource;

import java.util.List;

public record  UserPageResource(
        @JsonProperty("page")
        int page,
        @JsonProperty("size")
        int size,
        @JsonProperty("users")
        List<UserResource> content
) {

}
