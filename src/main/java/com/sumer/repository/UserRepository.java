package com.sumer.repository;

import com.sumer.entity.User;
import com.sumer.entity.UserConsent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
