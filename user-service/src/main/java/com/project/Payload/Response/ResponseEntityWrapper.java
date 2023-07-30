package com.project.Payload.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import java.util.List;

public class ResponseEntityWrapper<T> {
    @JsonProperty("data")
    private List<T> data;
    @JsonProperty("link")
    private List<Link> link;
    private String message;

    public ResponseEntityWrapper() {
    }

    public ResponseEntityWrapper(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
