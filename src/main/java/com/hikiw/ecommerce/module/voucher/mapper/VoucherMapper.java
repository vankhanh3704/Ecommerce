package com.hikiw.ecommerce.module.voucher.mapper;


import com.hikiw.ecommerce.module.voucher.dto.VoucherCreationRequest;
import com.hikiw.ecommerce.module.voucher.dto.VoucherResponse;
import com.hikiw.ecommerce.module.voucher.dto.VoucherUpdateRequest;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Mapping(target = "voucherId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "usages", ignore = true)
    VoucherEntity toEntity(VoucherCreationRequest entity);

    @Mapping(target = "voucherId", source = "voucherId")
    @Mapping(target = "isValid", expression = "java(entity.isValid())")
    @Mapping(target = "isActive", source = "isActive")
    VoucherResponse toResponse(VoucherEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVoucher(@MappingTarget VoucherEntity entity, VoucherUpdateRequest updateRequest);

    List<VoucherResponse> toResponseList(List<VoucherEntity> entities);
}
