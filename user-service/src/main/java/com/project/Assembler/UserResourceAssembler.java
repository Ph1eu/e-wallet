package com.project.Assembler;
import com.project.Controller.AdminController;
import com.project.Controller.UserController;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.DTO.AddressDTO;
import com.project.Payload.DTO.PaymentcardDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import com.project.Payload.Request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.Payload.Response.PaginationInfor;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Payload.Response.ResponsePagedEntityWrapper;
import jakarta.annotation.Nonnull;
import org.hibernate.engine.spi.EntityUniqueKey;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class UserResourceAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel( UserDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOneUser(entity.getUsername())).withSelfRel());
//                linkTo(methodOn(AdminController.class).getAllUsers()).withRel("Get All user for admin"));
    }

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities) {
        List<EntityModel<UserDTO>> entityModels = new ArrayList<>();
        for (UserDTO entity : entities) {
            EntityModel<UserDTO> userEntityModel = toModel(entity);
            entityModels.add(userEntityModel);
        }
        return CollectionModel.of(entityModels);
    }
    public ResponseEntityWrapper<EntityModel<UserDTO>> toCollectionModelInWrapper(List<UserDTO> entities){


        List<EntityModel<UserDTO>> entityModels = new ArrayList<>();
        for(UserDTO userDTO:entities){
            entityModels.add(toModel(userDTO));
        }
        ResponseEntityWrapper<EntityModel<UserDTO>> entityWrapper = new ResponseEntityWrapper<>();
        entityWrapper.setData(entityModels);
        entityWrapper.setLink(List.of(linkTo(methodOn(AdminController.class).getAllUsers("email",1000,0,5)).withRel("Get All user for admin")));
        return entityWrapper;
    }
    public ResponsePagedEntityWrapper<EntityModel<UserDTO>> toCollectionModelInPagedWrapper(Page<UserDTO> page){

        List<UserDTO> entities = page.getContent();
        List<EntityModel<UserDTO>> entityModels = new ArrayList<>();
        for(UserDTO userDTO:entities){
            entityModels.add(toModel(userDTO));
        }
        ResponsePagedEntityWrapper<EntityModel<UserDTO>> entityWrapper = new ResponsePagedEntityWrapper<>();
        entityWrapper.setData(entityModels);
        PaginationInfor paginationInfor = new PaginationInfor((int)page.getTotalElements(),
                                                                page.getTotalPages(),
                                                                page.getNumber(),
                                                                page.getSize(),
                                                                page.getNumberOfElements());
        entityWrapper.setPaginationInfo(paginationInfor);

        entityWrapper.setLink(List.of(linkTo(methodOn(AdminController.class).getAllUsers("email",1000,0,5)).withRel("Get All user for admin")));
        return entityWrapper;
    }
    public ResponseEntityWrapper<EntityModel<AddressDTO>>  toAddressModel(UserDTO entity){
        EntityModel<AddressDTO> addressEntityModel = EntityModel.of(entity.getAddress());
        ResponseEntityWrapper<EntityModel<AddressDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
        responseEntityWrapper.setData(List.of(addressEntityModel));
        BindingResult bindingResult=null;
        responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getAddress(entity.getUsername())).withRel("Get User Address"),
                linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),entity.getUsername(),bindingResult)).withRel("Set User Address").withType("POST")));
        return responseEntityWrapper;
    }
    public ResponseEntityWrapper<EntityModel<PaymentcardDTO>> toCardsCollectionModel(Iterable<PaymentcardDTO> entities, UserDTO entity) throws ParseException {
        List<EntityModel<PaymentcardDTO>> entityModels = new ArrayList<>();
        for (PaymentcardDTO paymentcard: entities){
            EntityModel<PaymentcardDTO> paymentcardEntityModel = EntityModel.of(paymentcard);
            entityModels.add(paymentcardEntityModel);
        }
        ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
        responseEntityWrapper.setData(entityModels);
        BindingResult bindingResult = null;
        List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
        responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getPaymentCards(entity.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).setPaymentCards(entity.getUsername(),paymentcardCRUDS,bindingResult)).withRel("Set information for current cards").withType("POST"),
                linkTo(methodOn(UserController.class).deletePaymentCardbyID(entity.getUsername(),"id")).withRel("delete one card").withType("DELETE"),
                linkTo(methodOn(UserController.class).deleteAllPaymentCard(entity.getUsername())).withRel("delete all cards").withType("DELETE")));
        return responseEntityWrapper;

    }
}
