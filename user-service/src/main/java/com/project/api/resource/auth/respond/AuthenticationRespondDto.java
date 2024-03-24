package com.project.api.resource.auth.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public record AuthenticationRespondDto(@JsonProperty("token") String token) {
}
