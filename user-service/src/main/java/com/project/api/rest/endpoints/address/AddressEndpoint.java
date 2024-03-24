package com.project.api.rest.endpoints.address;

import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.address.AddressResource;
import com.project.api.resource.address.model.AddressResourceDto;
import com.project.api.resource.address.request.AddressCreateRequestDto;
import com.project.api.resource.address.request.AddressFilterRequestDto;

public class AddressEndpoint implements AddressResource {
    @Override
    public ResponseEntityWrapper<AddressResourceDto> getByID(String id, String userid) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<AddressResourceDto> list(String userid, AddressFilterRequestDto addressFilterRequestDto) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<AddressResourceDto> create(AddressCreateRequestDto addressCreateRequestDto, String userid) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<AddressResourceDto> update(String id, AddressCreateRequestDto addressCreateRequestDto, String userid) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<AddressResourceDto> delete(String id, String userid) {
        return null;
    }
}
