package com.ensportive.licenses;

import com.ensportive.licenses.dtos.LicenseDTO;
import com.ensportive.licenses.dtos.LicenseRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LicenseMapper {

    default boolean isActive(LicenseEntity licenseEntity) {
        return licenseEntity.isActive();
    }

    @Mapping(target = "active", expression = "java(isActive(licenseEntity))")
    LicenseDTO map(LicenseEntity licenseEntity);

    LicenseEntity map(LicenseRequestDTO licenseRequestDTO);

}