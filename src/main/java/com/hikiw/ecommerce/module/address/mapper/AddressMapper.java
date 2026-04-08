package com.hikiw.ecommerce.module.address.mapper;


import com.hikiw.ecommerce.module.address.dto.AddressRequest;
import com.hikiw.ecommerce.module.address.dto.AddressResponse;
import com.hikiw.ecommerce.module.address.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    AddressEntity toEntity(AddressRequest request);

    // Ép MapStruct tự động nối chuỗi tạo ra biến fullAddress
    @Mapping(target = "fullAddress", expression = "java(entity.getStreetDetail() + \", \" + entity.getWard() + \", \" + entity.getDistrict() + \", \" + entity.getCity())")
    AddressResponse toResponse(AddressEntity entity);

    void updateEntityFromRequest(AddressRequest request, @MappingTarget AddressEntity entity);
}