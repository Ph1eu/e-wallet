package com.project.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Setter
@Getter
@Builder
public class ResponseEntityWrapper<T> extends ResponseEntity<T> {
    @JsonProperty("data")
    private List<T> data;
    @JsonProperty("link")
    private List<Link> link;
    private String message;

}

