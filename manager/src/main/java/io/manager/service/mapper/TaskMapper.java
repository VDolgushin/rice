package io.manager.service.mapper;

import io.manager.dto.CrackHashRequestBody;
import io.manager.dto.CrackHashTaskRequestBody;
import io.manager.entity.TaskEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toModel(CrackHashRequestBody crackHashRequestBody, @MappingTarget TaskEntity taskEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toModel(TaskEntity taskEntity, @MappingTarget CrackHashRequestBody crackHashRequestBody);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toModel(TaskEntity taskEntity, @MappingTarget CrackHashTaskRequestBody crackHashTaskRequestBody);
}
