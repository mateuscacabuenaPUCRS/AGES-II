package com.ensportive.requests;

import com.ensportive.requests.dtos.RequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDTO map(RequestEntity requestEntity);

    List<RequestDTO> map(List<RequestEntity> requestEntities);

    RequestEntity map(RequestDTO requestDTO);

}