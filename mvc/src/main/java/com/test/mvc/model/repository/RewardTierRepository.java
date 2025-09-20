package com.test.mvc.model.repository;

import com.test.mvc.model.entity.RewardTier;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardTierRepository extends JpaRepository<RewardTier, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RewardTier r where r.id = :id")
    Optional<RewardTier> lockById(@Param("id") Long id);

}
