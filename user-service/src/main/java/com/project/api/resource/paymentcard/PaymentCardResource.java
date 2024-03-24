package com.project.api.resource.paymentcard;

import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;
import com.project.api.resource.paymentcard.request.PaymentCardFilterRequestDto;
import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.paymentcard.request.PaymentCardUpdateRequestDto;
import com.project.api.resource.paymentcard.respond.PagePaymentCardRespondDto;
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
    ResponseEntityWrapper<PaymentCardResourceDto> update(@PathVariable String id, @RequestBody PaymentCardUpdateRequestDto paymentCardUpdateRequestDto, @PathVariable String userId);
    @DeleteMapping("/{id}")
    ResponseEntityWrapper<PaymentCardResourceDto> delete(@PathVariable String id, @PathVariable String userId);
    @GetMapping()
    ResponseEntityWrapper<PagePaymentCardRespondDto> list(@Valid @NotNull @BeanParam PaymentCardFilterRequestDto paymentCardFilterRequestDto, @PathVariable String userId);




}
