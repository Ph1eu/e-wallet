package com.project.api.resource.address;

import com.project.api.resource.address.model.AddressResourceDto;
import com.project.api.resource.address.request.AddressCreateRequestDto;
import com.project.api.resource.address.request.AddressFilterRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user/{userid}/address")
public interface AddressResource {
    @GetMapping("/{id}")
    ResponseEntityWrapper<AddressResourceDto> getByID(@PathVariable String id, @PathVariable String userid);
    @GetMapping("")
    ResponseEntityWrapper<AddressResourceDto> list(@PathVariable String userid, @NotNull@Valid@BeanParam AddressFilterRequestDto addressFilterRequestDto);
    @PostMapping("")
    ResponseEntityWrapper<AddressResourceDto> create(@NotNull@Valid AddressCreateRequestDto addressCreateRequestDto, @PathVariable String userid);
    @PutMapping("/{id}")
    ResponseEntityWrapper<AddressResourceDto> update(@PathVariable String id, AddressCreateRequestDto addressCreateRequestDto, @PathVariable String userid);
    @DeleteMapping("/{id}")
    ResponseEntityWrapper<AddressResourceDto> delete(@PathVariable String id, @PathVariable String userid);


}
