package com.project.Assembler;

import com.project.Controller.UserController;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Payload.DTO.UserDTO;

import com.project.Payload.Response.ResponseEntityWrapper;
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
public class BalanceResourceAssembler implements RepresentationModelAssembler<BalanceInformationDTO, EntityModel<BalanceInformationDTO>> {



    @Override
    public EntityModel<BalanceInformationDTO> toModel(BalanceInformationDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getOnlineBalance(entity.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(entity.getUser(),(int)10000)).withRel("deposit money to wallet example : 10000"),
        linkTo(methodOn(UserController.class).withdrawalMoney(entity.getUser(),(int)10000)).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(entity.getUser(),(int)10000,"someone's phone number")).withRel("transfer money to someone example : 10000"));


    }

    @Override
    public CollectionModel<EntityModel<BalanceInformationDTO>> toCollectionModel(Iterable<? extends BalanceInformationDTO> entities) {

        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
    public ResponseEntityWrapper<BalanceInformationDTO> toModelWithWrapper(BalanceInformationDTO entity) {
        ResponseEntityWrapper<BalanceInformationDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<BalanceInformationDTO>();
        entityModelResponseEntityWrapper.setData(List.of(entity));
        entityModelResponseEntityWrapper.setLink(List.of( linkTo(methodOn(UserController.class).getOnlineBalance(entity.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(entity.getUser(),(int)10000)).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(entity.getUser(),(int)10000)).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(entity.getUser(),(int)10000,"someone's phone number")).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;


    }
    public ResponseEntityWrapper<TransactionHistoryDTO> toModelByTransaction(BalanceInformationDTO entity, TransactionHistoryDTO historyDTO) {
        ResponseEntityWrapper<TransactionHistoryDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<TransactionHistoryDTO>();
        entityModelResponseEntityWrapper.setData(List.of(historyDTO));
        entityModelResponseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getOnlineBalance(entity.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(entity.getUser(),(int)10000)).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(entity.getUser(),(int)10000)).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(entity.getUser(),(int)10000,"someone's phone number")).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;


    }
    public ResponseEntityWrapper<TransactionHistoryDTO> toModelByTransferTransaction(BalanceInformationDTO sender,BalanceInformationDTO recipient, TransactionHistoryDTO historyDTO) {
        ResponseEntityWrapper<TransactionHistoryDTO> entityModelResponseEntityWrapper = new ResponseEntityWrapper<TransactionHistoryDTO>();
        entityModelResponseEntityWrapper.setData(List.of(historyDTO));
        entityModelResponseEntityWrapper.setLink(List.of( linkTo(methodOn(UserController.class).getOnlineBalance(sender.getUser())).withSelfRel(),
                linkTo(methodOn(UserController.class).depositMoney(sender.getUser(),(int)10000)).withRel("deposit money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).withdrawalMoney(sender.getUser(),(int)10000)).withRel("withdrawal money to wallet example : 10000"),
                linkTo(methodOn(UserController.class).transferMoney(recipient.getUser(),(int)10000,recipient.getPhone_number())).withRel("transfer money to someone example : 10000")));

        return entityModelResponseEntityWrapper;



    }
}
