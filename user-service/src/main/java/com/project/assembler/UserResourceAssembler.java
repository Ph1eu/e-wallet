package com.project.assembler;

import com.project.controller.AdminController;
import com.project.controller.UserController;
import com.project.service.address.dto.AddressDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.user.dto.UserDto;
import com.project.payload.request.CRUDUserInforRequest.AddressCRUD;
import com.project.payload.request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.payload.response.PaginationInfor;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.payload.response.ResponsePagedEntityWrapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {
    @Override
    public EntityModel<UserDto> toModel(UserDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOneUser(entity.getUsername())).withSelfRel());
//                linkTo(methodOn(AdminController.class).getAllUsers()).withRel("Get All user for admin"));
    }

    @Override
    public CollectionModel<EntityModel<UserDto>> toCollectionModel(Iterable<? extends UserDto> entities) {
        List<EntityModel<UserDto>> entityModels = new ArrayList<>();
        for (UserDto entity : entities) {
            EntityModel<UserDto> userEntityModel = toModel(entity);
            entityModels.add(userEntityModel);
        }
        return CollectionModel.of(entityModels);
    }

    public ResponseEntityWrapper<EntityModel<UserDto>> toCollectionModelInWrapper(List<UserDto> entities) {


        List<EntityModel<UserDto>> entityModels = new ArrayList<>();
        for (UserDto userDTO : entities) {
            entityModels.add(toModel(userDTO));
        }
        ResponseEntityWrapper<EntityModel<UserDto>> entityWrapper = new ResponseEntityWrapper<>();
        entityWrapper.setData(entityModels);
        entityWrapper.setLink(List.of(linkTo(methodOn(AdminController.class).getAllUsers("email", 1000, 0, 5)).withRel("Get All user for admin")));
        return entityWrapper;
    }

    public ResponsePagedEntityWrapper<EntityModel<UserDto>> toCollectionModelInPagedWrapper(Page<UserDto> page) {

        List<UserDto> entities = page.getContent();
        List<EntityModel<UserDto>> entityModels = new ArrayList<>();
        for (UserDto userDTO : entities) {
            entityModels.add(toModel(userDTO));
        }
        ResponsePagedEntityWrapper<EntityModel<UserDto>> entityWrapper = new ResponsePagedEntityWrapper<>();
        entityWrapper.setData(entityModels);
        PaginationInfor paginationInfor = new PaginationInfor((int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements());
        entityWrapper.setPaginationInfo(paginationInfor);

        entityWrapper.setLink(List.of(linkTo(methodOn(AdminController.class).getAllUsers("email", 1000, 0, 5)).withRel("Get All user for admin")));
        return entityWrapper;
    }

    public ResponseEntityWrapper<EntityModel<AddressDto>> toAddressModel(UserDto entity) {
        EntityModel<AddressDto> addressEntityModel = EntityModel.of(entity.getAddressDTO());
        ResponseEntityWrapper<EntityModel<AddressDto>> responseEntityWrapper = new ResponseEntityWrapper<>();
        responseEntityWrapper.setData(List.of(addressEntityModel));
        BindingResult bindingResult = null;
        responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getAddress(entity.getUsername())).withRel("Get User Address"),
                linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(), entity.getUsername(), bindingResult)).withRel("Set User Address").withType("POST")));
        return responseEntityWrapper;
    }

    public ResponseEntityWrapper<EntityModel<PaymentcardDTO>> toCardsCollectionModel(Iterable<PaymentcardDTO> entities, UserDto entity) throws ParseException {
        List<EntityModel<PaymentcardDTO>> entityModels = new ArrayList<>();
        for (PaymentcardDTO paymentcard : entities) {
            EntityModel<PaymentcardDTO> paymentcardEntityModel = EntityModel.of(paymentcard);
            entityModels.add(paymentcardEntityModel);
        }
        ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
        responseEntityWrapper.setData(entityModels);
        BindingResult bindingResult = null;
        List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
        responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getPaymentCards(entity.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).setPaymentCards(entity.getUsername(),
                        paymentcardCRUDS,
                        bindingResult)).withRel("Set information for current cards").withType("POST"),
                linkTo(methodOn(UserController.class).deletePaymentCardbyID(entity.getUsername(), "id"))
                        .withRel("delete one card").withType("DELETE"),
                linkTo(methodOn(UserController.class).deleteAllPaymentCard(entity.getUsername())).withRel("delete all cards").withType("DELETE")));
        return responseEntityWrapper;

    }
}
