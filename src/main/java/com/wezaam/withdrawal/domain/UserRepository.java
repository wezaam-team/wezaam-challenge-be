package com.wezaam.withdrawal.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from com.wezaam.withdrawal.domain.User user left join fetch user.paymentMethods where user.id = :userId")
    Optional<User> findByIdWithPaymentMethods(@Param("userId") Long userId);
}
