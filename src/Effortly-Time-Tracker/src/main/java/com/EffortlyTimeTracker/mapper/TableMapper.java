package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.enums.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableMapper {

    public TableEntity dtoToEntity(TableDTO tableDTO) {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName(tableDTO.getName());
        tableEntity.setDescription(tableDTO.getDescription());
        tableEntity.setStatus(Status.valueOf(tableDTO.getStatus()));
        tableEntity.setProject(tableDTO.getProject());

        return tableEntity;
    }

    public TableDTO entityToDto(TableEntity tableEntity) {
        TableDTO tableDTO = new TableDTO();
        tableDTO.setName(tableEntity.getName());
        tableDTO.setDescription(tableEntity.getDescription());
        tableDTO.setStatus(tableEntity.getStatus().name());
        tableDTO.setProject(tableEntity.getProject());
        return tableDTO;
    }

    public List<TableDTO> entityListToDtoList(List<TableEntity> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

}
