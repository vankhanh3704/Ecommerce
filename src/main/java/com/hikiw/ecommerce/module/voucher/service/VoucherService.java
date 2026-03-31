package com.hikiw.ecommerce.module.voucher.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.voucher.dto.VoucherCreationRequest;
import com.hikiw.ecommerce.module.voucher.dto.VoucherResponse;
import com.hikiw.ecommerce.module.voucher.dto.VoucherUpdateRequest;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import com.hikiw.ecommerce.module.voucher.mapper.VoucherMapper;
import com.hikiw.ecommerce.module.voucher.repository.VoucherRepository;
import com.hikiw.ecommerce.module.voucher.repository.VoucherUsageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional(readOnly = true)
public class VoucherService {
    VoucherRepository voucherRepository;
    VoucherUsageRepository voucherUsageRepository;
    VoucherMapper voucherMapper;

    @Transactional
    public VoucherResponse createVoucher(VoucherCreationRequest request){
        if(voucherRepository.existsByCode(request.getCode())){
            throw new AppException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
        }
        // Kiểm tra ngày kết thúc có trước ngày bắt đầu không
        if(request.getEndDate().isBefore(request.getStartDate())){
            throw new AppException(ErrorCode.VOUCHER_END_DATE_BEFORE_START_DATE);
        }
        VoucherEntity voucherEntity = voucherMapper.toEntity(request);
        voucherEntity.setCode(request.getCode().toUpperCase()); // Mã voucher luôn được lưu dưới dạng chữ hoa
        return voucherMapper.toResponse(voucherRepository.save(voucherEntity));
    }

    @Transactional
    public VoucherResponse updateVoucher(Long voucherId, VoucherUpdateRequest request){
        VoucherEntity voucherEntity = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

        voucherMapper.updateVoucher(voucherEntity, request);
        return voucherMapper.toResponse(voucherRepository.save(voucherEntity));

    }

    @Transactional
    public void deleteVoucher(Long voucherId){
        VoucherEntity entity = voucherRepository.findById(voucherId).orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
        entity.setIsActive(false);
        voucherRepository.save(entity);
    }

    public VoucherResponse getVoucherById(Long voucherId){
        return voucherRepository.findById(voucherId)
                .map(voucherMapper::toResponse)
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
    }

    public VoucherResponse getVoucherByCode(String code){
        return voucherRepository.findByCode(code.toUpperCase())
                .map(voucherMapper::toResponse)
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
    }

    public List<VoucherResponse> getAllVouchers(){
        return voucherMapper.toResponseList(voucherRepository.findAll());
    }

}
