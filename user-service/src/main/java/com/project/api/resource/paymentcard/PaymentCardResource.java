package com.project.api.resource.paymentcard;

import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;
import com.project.api.resource.paymentcard.request.PaymentCardFilterDto;
import com.project.api.common.model.ResponseEntityWrapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user/{userId}/cards")
public interface PaymentCardResource {
    @GetMapping("/{id}")
    ResponseEntityWrapper<PaymentCardResourceDto> getByID(@PathVariable String id, @PathVariable String userId);
    @PostMapping()
    ResponseEntityWrapper<PaymentCardResourceDto> create(PaymentCardResourceDto paymentCardResourceDto, @PathVariable String userId);
    @PutMapping("/{id}")
    ResponseEntityWrapper<PaymentCardResourceDto> update(@PathVariable String id, PaymentCardResourceDto paymentCardResourceDto, @PathVariable String userId);
    @DeleteMapping("/{id}")
    ResponseEntityWrapper<PaymentCardResourceDto> delete(@PathVariable String id, @PathVariable String userId);
    @GetMapping()
    ResponseEntityWrapper<PaymentCardResourceDto> list(@Valid @NotNull @BeanParam PaymentCardFilterDto paymentCardFilterDto, @PathVariable String userId);




}
