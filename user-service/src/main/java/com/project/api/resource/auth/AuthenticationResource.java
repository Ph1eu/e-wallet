package com.project.api.resource.auth;

import com.project.api.resource.auth.request.AuthenticationRequestDto;
import com.project.api.resource.auth.request.RegisterRequestDto;
import com.project.api.resource.auth.respond.AuthenticationRespondDto;
import com.project.api.common.model.ResponseEntityWrapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public interface AuthenticationResource {
    @PostMapping("")
    ResponseEntityWrapper<AuthenticationRespondDto> authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto);
    @PostMapping("/signup")
    ResponseEntityWrapper<AuthenticationRespondDto> register(@RequestBody RegisterRequestDto registerRequestDto);
}
