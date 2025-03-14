package io.manager.service.mapper;

import io.manager.dto.RequestStatusResponse;
import io.manager.entity.RequestEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toModel(RequestEntity requestEntity, @MappingTarget RequestStatusResponse requestStatusResponse);

}
