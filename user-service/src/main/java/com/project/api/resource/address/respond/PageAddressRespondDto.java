package com.project.api.resource.address.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.resource.address.model.AddressResourceDto;
import lombok.Data;

import java.util.List;

@Data
public class PageAddressRespondDto {
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("content")
    private List<AddressResourceDto> content;
}
