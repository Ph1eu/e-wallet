package com.project.Assembler;

import com.project.Controller.HourlyReportController;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Payload.Response.PaginationInfor;
import com.project.Payload.Response.ResponsePagedEntityWrapper;
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
public class HourlyReportAssembler implements RepresentationModelAssembler<HourlyReportDTO, EntityModel<HourlyReportDTO>> {
    @Override
    public EntityModel<HourlyReportDTO> toModel(HourlyReportDTO entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<HourlyReportDTO>> toCollectionModel(Iterable<? extends HourlyReportDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
    public ResponsePagedEntityWrapper<EntityModel<HourlyReportDTO>> toCollectionModelwithWrapper(Page<HourlyReportDTO> page){
        List<HourlyReportDTO> hourlyReportDTOS = page.getContent();
        List<EntityModel<HourlyReportDTO>> entityModels = new ArrayList<>();
        ResponsePagedEntityWrapper<EntityModel<HourlyReportDTO>> responsePagedEntityWrapper = new ResponsePagedEntityWrapper<>();
        for (HourlyReportDTO hourlyReportDTO : hourlyReportDTOS){
            entityModels.add(toModel(hourlyReportDTO));
        }
        responsePagedEntityWrapper.setData(entityModels);
        PaginationInfor paginationInfor = new PaginationInfor((int)page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements());
        responsePagedEntityWrapper.setPaginationInfo(paginationInfor);
        //responsePagedEntityWrapper.setLink(List.of(linkTo(methodOn(HourlyReportController.class).getHistory(username,0,10)).withSelfRel()));
        return responsePagedEntityWrapper;
    }
}
