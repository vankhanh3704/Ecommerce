package com.hikiw.ecommerce.module.voucher.service;


import com.hikiw.ecommerce.Enum.DiscountType;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.shop.repository.ShopRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import com.hikiw.ecommerce.module.voucher.dto.*;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherUsageEntity;
import com.hikiw.ecommerce.module.voucher.mapper.VoucherMapper;
import com.hikiw.ecommerce.module.voucher.repository.VoucherRepository;
import com.hikiw.ecommerce.module.voucher.repository.VoucherUsageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    UserRepository userRepository;

    ShopRepository shopRepository; // Thêm ShopRepo
    SecurityUtil securityUtils; // Thêm SecurityUtil để lấy thông tin user hiện tại

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

        if (voucherEntity.getShop() != null) {
            ShopEntity shop = shopRepository.findById(voucherEntity.getShop().getShopId())
                    .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));
            validateShopOwnership(shop);
        }

        voucherEntity.setCode(request.getCode().toUpperCase()); // Mã voucher luôn được lưu dưới dạng chữ hoa
        return voucherMapper.toResponse(voucherRepository.save(voucherEntity));
    }

    @Transactional
    public VoucherResponse updateVoucher(Long voucherId, VoucherUpdateRequest request){
        VoucherEntity voucherEntity = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

        validateShopOwnership(voucherEntity.getShop());
        voucherMapper.updateVoucher(voucherEntity, request);
        return voucherMapper.toResponse(voucherRepository.save(voucherEntity));

    }

    @Transactional
    public void deleteVoucher(Long voucherId){
        VoucherEntity entity = voucherRepository.findById(voucherId).orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
        // BẢO MẬT: Phải là chủ shop mới được xóa
        validateShopOwnership(entity.getShop());
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


    // APPlY VOUCHER
    public ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request){
        VoucherEntity voucher = voucherRepository.findByCode(request.getCode().toUpperCase())
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

        // kiểm tra xem voucher còn hiệu lực không,
        if(!voucher.isValid()){
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }


        if (voucher.getShop() != null) {
            if (request.getShopId() == null || !voucher.getShop().getShopId().equals(request.getShopId())) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Thay bằng mã lỗi VOUCHER_NOT_FOR_THIS_SHOP
            }
        }

        int userUsedCount = voucherUsageRepository.countByVoucher_VoucherIdAndUser_Id(voucher.getVoucherId(), request.getUserId());
        if(userUsedCount >= voucher.getUserUsageLimit()){
            throw new AppException(ErrorCode.VOUCHER_ALREADY_USED); // Hoặc mã lỗi VOUCHER_USER_LIMIT_REACHED
        }

        // kiểm tra đơn hàng có đạt mức tối thiểu không
        if (request.getOrderAmount() < voucher.getMinSpend()) {
            throw new AppException(ErrorCode.VOUCHER_MIN_SPEND_NOT_MET);
        }

        // tính số tiền giảm được
        Double discountAmount = voucher.calculateDiscount(request.getOrderAmount());
        Double finalAmount = request.getOrderAmount() - discountAmount; // giá sản phẩm sau khi giảm giá

        boolean isFreeShipping = voucher.getDiscountType() == DiscountType.FREE_SHIPPING;

        return ApplyVoucherResponse.builder()
                .voucherId(voucher.getVoucherId())
                .code(voucher.getCode())
                .discountType(voucher.getDiscountType())
                .discountValue(voucher.getDiscountValue())
                .orderAmount(request.getOrderAmount())
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .isFreeShipping(isFreeShipping)
                .build();
    }

    public List<VoucherResponse> getAllValidVouchers() {
        return voucherMapper.toResponseList(
                voucherRepository.findAllValid(LocalDateTime.now())
        );
    }

    @Transactional
    public void confirmVoucherUsage(Long voucherId, Long userId, Long orderId) {
        VoucherEntity voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Hibernate sẽ tự động ném ra ObjectOptimisticLockingFailureException
        // nếu 2 request cùng lúc cố gắng ghi đè số usedCount của voucher này.
        VoucherUsageEntity usage = VoucherUsageEntity.builder()
                .voucher(voucher)
                .user(user)
                .orderId(orderId)
                .build();
        voucherUsageRepository.save(usage);

        // Tăng used_count
        voucher.setUsedCount(voucher.getUsedCount() + 1);
        voucherRepository.save(voucher);

        log.info("Voucher {} confirmed for order {}", voucher.getCode(), orderId);
    }

    private void validateShopOwnership(ShopEntity shop) {
        if (shop != null) {
            Long currentUserId = securityUtils.getCurrentUserId();
            // Nếu không phải là chủ shop (và giả sử chưa xét Role Admin ở đây) thì chặn
            if (!shop.getOwner().getId().equals(currentUserId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED_ACTION); // Hoặc tạo mã lỗi SHOP_ACCESS_DENIED
            }
        }
    }
}
