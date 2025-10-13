package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    Integer deleteByExpiryTimeBefore(Date now);
}
