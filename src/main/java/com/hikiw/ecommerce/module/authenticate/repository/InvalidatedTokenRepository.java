package com.hikiw.ecommerce.module.authenticate.repository;

import com.hikiw.ecommerce.module.authenticate.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    Integer deleteByExpiryTimeBefore(Date now);
}
