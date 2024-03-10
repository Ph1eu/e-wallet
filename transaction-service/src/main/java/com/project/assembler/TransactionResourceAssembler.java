package com.project.assembler;

import com.project.controller.UserController;
import com.project.payload.dto.TransactionHistoryDTO;
import com.project.payload.response.PaginationInfor;
import com.project.payload.response.ResponsePagedEntityWrapper;
import org.springframework.data.domain.Page;
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
                linkTo(methodOn(UserController.class).getHistory("username", "1", "10")).withSelfRel());

    }

    public ResponsePagedEntityWrapper<EntityModel<TransactionHistoryDTO>> toCollectionModelWithUsername(Page<TransactionHistoryDTO> page, String username) {
        List<TransactionHistoryDTO> entities = page.getContent();
        List<EntityModel<TransactionHistoryDTO>> entityModels = new ArrayList<>();
        ResponsePagedEntityWrapper<EntityModel<TransactionHistoryDTO>> entityWrapper = new ResponsePagedEntityWrapper<>();
        for (TransactionHistoryDTO transactionHistoryDTO : entities) {
            entityModels.add(toModel(transactionHistoryDTO));
        }
        entityWrapper.setData(entityModels);
        PaginationInfor paginationInfor = new PaginationInfor((int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements());
        entityWrapper.setPaginationInfo(paginationInfor);
        entityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getHistory(username, "0", "10")).withSelfRel()));
        return entityWrapper;

    }

    public ResponsePagedEntityWrapper<EntityModel<TransactionHistoryDTO>> toCollectionModelInPagedWrapper(Page<TransactionHistoryDTO> page) {

        List<TransactionHistoryDTO> entities = page.getContent();
        List<EntityModel<TransactionHistoryDTO>> entityModels = new ArrayList<>();
        for (TransactionHistoryDTO transactionHistoryDTO : entities) {
            entityModels.add(toModel(transactionHistoryDTO));
        }
        ResponsePagedEntityWrapper<EntityModel<TransactionHistoryDTO>> entityWrapper = new ResponsePagedEntityWrapper<>();
        entityWrapper.setData(entityModels);
        PaginationInfor paginationInfor = new PaginationInfor((int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements());
        entityWrapper.setPaginationInfo(paginationInfor);

        return entityWrapper;
    }
}
