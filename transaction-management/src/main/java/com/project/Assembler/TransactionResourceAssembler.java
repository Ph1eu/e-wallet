package com.project.Assembler;

import com.project.Controller.UserController;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Payload.Response.ResponseEntityWrapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class TransactionResourceAssembler implements RepresentationModelAssembler<TransactionHistoryDTO, EntityModel<TransactionHistoryDTO>> {

    @Override
    public EntityModel<TransactionHistoryDTO> toModel(TransactionHistoryDTO entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<TransactionHistoryDTO>> toCollectionModel(Iterable<? extends TransactionHistoryDTO> entities) {
        return CollectionModel.of(RepresentationModelAssembler.super.toCollectionModel(entities),
                linkTo(methodOn(UserController.class).getHistory("username")).withSelfRel());

    }
    public ResponseEntityWrapper<EntityModel<TransactionHistoryDTO>> toCollectionModelWithUsername(List<TransactionHistoryDTO> entities, String username) {
        List<EntityModel<TransactionHistoryDTO>> entityModels = new ArrayList<>();
        for(TransactionHistoryDTO transactionHistoryDTO:entities){
            entityModels.add(toModel(transactionHistoryDTO));
        }
        ResponseEntityWrapper<EntityModel<TransactionHistoryDTO>> responseEntityWrapper = new ResponseEntityWrapper<EntityModel<TransactionHistoryDTO>>();
        responseEntityWrapper.setData(entityModels);
        responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getHistory(username)).withSelfRel()));
        return responseEntityWrapper;

    }
}
