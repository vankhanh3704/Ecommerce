package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
