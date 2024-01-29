package com.project.assembler;

import com.project.controller.UserController;
import com.project.payload.dto.BalanceInformationDTO;
import com.project.payload.dto.TransactionHistoryDTO;

import com.project.payload.response.ResponseEntityWrapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BalanceResourceAssembler implements RepresentationModelAssembler<BalanceInformationDTO, EntityModel<BalanceInformationDTO>> {



    @Override
    public EntityModel<BalanceInformationDTO> toModel(BalanceInformationDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOnlineBalance(entity.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(entity.getUser(),"10000")).withRel("deposit money to wallet example : 10000"),
        linkTo(methodOn(UserController.class).withdrawalMoney(entity.getUser(),"10000")).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(entity.getUser(),"10000","someone's phone number")).withRel("transfer money to someone example : 10000"));


    }

    @Override
    public CollectionModel<EntityModel<BalanceInformationDTO>> toCollectionModel(Iterable<? extends BalanceInformationDTO> entities) {

        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
    public ResponseEntityWrapper<BalanceInformationDTO> toModelWithWrapper(BalanceInformationDTO entity,String username) {
        ResponseEntityWrapper<BalanceInformationDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<BalanceInformationDTO>();
        entityModelResponseEntityWrapper.setData(List.of(entity));
        entityModelResponseEntityWrapper.setLink(List.of( linkTo(methodOn(UserController.class).getOnlineBalance(username)).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(username,"10000")).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(username,"10000")).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(username,"10000","someone's phone number")).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;


    }
    public ResponseEntityWrapper<TransactionHistoryDTO> toModelByTransaction(BalanceInformationDTO entity, TransactionHistoryDTO historyDTO,String username) {
        ResponseEntityWrapper<TransactionHistoryDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<TransactionHistoryDTO>();
        entityModelResponseEntityWrapper.setData(List.of(historyDTO));
        entityModelResponseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getOnlineBalance(username)).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(username,"10000")).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(username,"10000")).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(username,"10000","someone's phone number")).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;


    }
    public ResponseEntityWrapper<TransactionHistoryDTO> toModelByTransferTransaction(BalanceInformationDTO sender,BalanceInformationDTO recipient, TransactionHistoryDTO historyDTO,String username) {
        ResponseEntityWrapper<TransactionHistoryDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<TransactionHistoryDTO>();
        entityModelResponseEntityWrapper.setData(List.of(historyDTO));
        entityModelResponseEntityWrapper.setLink(List.of( linkTo(methodOn(UserController.class).getOnlineBalance(sender.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(username,"10000")).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(username,"10000")).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(username,"10000",recipient.getPhone_number())).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;



    }
}
