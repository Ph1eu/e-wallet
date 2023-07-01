package com.project.Payload.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import java.util.List;

public class ResponsePagedEntityWrapper<T> {
    @JsonProperty("data")
    private List<T> data;
    @JsonProperty("PaginationInfor")

    private PaginationInfor paginationInfo;
    @JsonProperty("link")
    private List<Link> link;

    public PaginationInfor getPaginationInfo() {
        return paginationInfo;
    }

    public void setPaginationInfo(PaginationInfor paginationInfo) {
        this.paginationInfo = paginationInfo;
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
}
