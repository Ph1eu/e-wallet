package com.project.api.resource.auth;

import com.project.api.resource.auth.respond.AuthenticationRespondDto;
import com.project.payload.response.ResponseEntityWrapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public interface AuthenticationResource {
    @PostMapping("")
    ResponseEntityWrapper<AuthenticationRespondDto> authenticate();
    @PostMapping("/signup")
    ResponseEntityWrapper<AuthenticationRespondDto> register();
}
