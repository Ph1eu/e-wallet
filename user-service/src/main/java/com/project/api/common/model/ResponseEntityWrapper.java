package com.project.api.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.resource.auth.respond.AuthenticationRespondDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder
public class ResponseEntityWrapper<T> extends ResponseEntity<T> {
    @JsonProperty("data")
    private T data;
    @JsonProperty("link")
    private List<Link> link;
    private String message;
    private HttpStatus status;
    public ResponseEntityWrapper(T body, HttpStatus status) {
        super(body, status);
        this.status = status;
    }



}

