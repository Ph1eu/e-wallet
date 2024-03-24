package com.project.api.resource.address.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public record AddressResourceDto(@JsonProperty("id") String id, @JsonProperty("street") String street,
                                 @JsonProperty("city") String city, @JsonProperty("province") String province,
                                 @JsonProperty("country") String country) {
}
