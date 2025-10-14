package com.hikiw.ecommerce.Scheduler;


import com.hikiw.ecommerce.Repository.InvalidatedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupScheduler {

    InvalidatedTokenRepository invalidatedTokenRepository;

    // Chạy mỗi ngày lúc 00:00 (nửa đêm)
//    @Scheduled(cron = "0 0 0 * * ?")

    @Scheduled(fixedRate = 5 * 60 * 1000) // dọn sau 5 phút
    @Transactional
    public void cleanExpiredTokens() {
        Integer deleted = invalidatedTokenRepository.deleteByExpiryTimeBefore(new Date());
        System.out.println("Đã xoá " + deleted + " token hết hạn.");
    }
}
