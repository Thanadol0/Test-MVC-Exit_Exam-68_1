package com.test.mvc.model.repository;

import com.test.mvc.model.entity.Pledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, Long> {

}
