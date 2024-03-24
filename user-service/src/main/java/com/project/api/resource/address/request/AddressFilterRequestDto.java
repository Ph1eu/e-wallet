package com.project.api.resource.address.request;

import com.project.api.common.model.PageRequestDto;
import jakarta.ws.rs.QueryParam;

public class AddressFilterRequestDto extends PageRequestDto {
    @QueryParam("street")
    public String street = null;
    @QueryParam("city")
    public String city = null;
    @QueryParam("province")
    public String province = null;
    @QueryParam("country")
    public String country = null;

}
