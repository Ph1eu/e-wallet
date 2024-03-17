package com.project.api.resource.auth.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthenticationRespondDto {
    @JsonProperty("token")
    private String token;
}
