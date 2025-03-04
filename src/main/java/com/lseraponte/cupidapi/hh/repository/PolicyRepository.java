package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository  extends JpaRepository<Policy, Integer> {

    Optional<Policy> findByPolicyTypeAndNameAndDescription(String policyType, String name, String description);

}
