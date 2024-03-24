package com.project.api.common.model;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class PageRequestDto {
    @QueryParam("page")
    private int page;
    @QueryParam("size")
    private int size;
}
