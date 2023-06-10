package com.project.Assembler;
import com.project.Controller.AdminController;
import com.project.Controller.UserController;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import com.project.Payload.Request.CRUDUserInforRequest.PaymentcardCRUD;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class UserResourceAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        EntityModel<UserDTO> userEntityModel = EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOneUser(entity.getUsername())).withSelfRel(),
                linkTo(methodOn(AdminController.class).getAllUsers()).withRel("Get All user for admin"));
        return userEntityModel;
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
    public EntityModel<Address>  toAddressModel(UserDTO entity){
        EntityModel<Address> addressEntityModel = EntityModel.of(entity.getAddress());
        addressEntityModel.add(linkTo(methodOn(UserController.class).getAddress(entity.getUsername())).withRel("Get User Address"));
        addressEntityModel.add(linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),entity.getUsername())).withRel("Set User Address").withType("POST"));
        return addressEntityModel;
    }
    public CollectionModel<EntityModel<Paymentcard>> toCardsCollectionModel(Iterable<Paymentcard> entities,UserDTO entity) throws ParseException {
        List<EntityModel<Paymentcard>> entityModels = new ArrayList<>();
        for (Paymentcard paymentcard: entities){
            EntityModel<Paymentcard> paymentcardEntityModel = EntityModel.of(paymentcard);
            entityModels.add(paymentcardEntityModel);
        }
        List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
        return CollectionModel.of(entityModels,
                linkTo(methodOn(UserController.class).getPaymentCards(entity.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).setPaymentCards(entity.getUsername(),paymentcardCRUDS)).withRel("Set information for current cards"),
                linkTo(methodOn(UserController.class).deletePaymentCardbyID(entity.getUsername(),"id")).withRel("delete one card"),
                linkTo(methodOn(UserController.class).deleteAllPaymentCard(entity.getUsername())).withRel("delete all cards")

        );
    }
}
