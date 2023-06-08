package com.project.Assembler;
import com.project.Controller.AdminController;
import com.project.Controller.UserController;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class UserResourceAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        EntityModel<User> userEntityModel = EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOneUser(entity.getUsername())).withSelfRel(),
                linkTo(methodOn(AdminController.class).getAllUsers()).withRel("Get All user for admin"));
        return userEntityModel;
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        List<EntityModel<User>> entityModels = new ArrayList<>();
        for (User entity : entities) {
            EntityModel<User> userEntityModel = toModel(entity);
            entityModels.add(userEntityModel);
        }
        return CollectionModel.of(entityModels);
    }
    public EntityModel<Address>  toAddressModel(User entity){
        EntityModel<Address> addressEntityModel = EntityModel.of(entity.getAddress());
        addressEntityModel.add(linkTo(methodOn(UserController.class).getAddress(entity.getUsername())).withRel("Get User Address"));
        addressEntityModel.add(linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),entity.getUsername())).withRel("Set User Address").withType("POST"));
        return addressEntityModel;
    }
    public CollectionModel<EntityModel<Paymentcard>> toCardsCollectionModel(Iterable<Paymentcard> entities){
        List<EntityModel<Paymentcard>> entityModels = new ArrayList<>();
        for (Paymentcard paymentcard: entities){
            EntityModel<Paymentcard> paymentcardEntityModel = EntityModel.of(paymentcard);
            entityModels.add(paymentcardEntityModel);
        }
        return CollectionModel.of(entityModels);
    }
}
