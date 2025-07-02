package com.sumer.repository;
import com.sumer.entity.InsuranceConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
@Repository
public interface InsuranceConsentRepository extends JpaRepository<InsuranceConsent, Long> {
    Optional<InsuranceConsent> findByUserId(Long userId);
}


