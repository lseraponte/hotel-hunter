package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository policyRepository;

    private Policy policy;

    @BeforeEach
    void setUp() {

        policy = Policy.builder()
                .policyType("Refund")
                .name("Cancellation Policy")
                .description("No refunds after 24 hours")
                .childAllowed("Yes")
                .petsAllowed("No")
                .parking("Free")
                .language("en")
                .build();

        policyRepository.save(policy);
    }

    @Test
    void testFindByPolicyTypeAndNameAndDescription() {

        Optional<Policy> foundPolicy = policyRepository.findByPolicyTypeAndNameAndDescription(
                policy.getPolicyType(), policy.getName(), policy.getDescription());

        assertTrue(foundPolicy.isPresent());
        assertEquals(policy.getPolicyType(), foundPolicy.get().getPolicyType());
        assertEquals(policy.getName(), foundPolicy.get().getName());
        assertEquals(policy.getDescription(), foundPolicy.get().getDescription());
    }

    @Test
    void testFindByPolicyTypeAndNameAndDescriptionNotFound() {

        Optional<Policy> foundPolicy = policyRepository.findByPolicyTypeAndNameAndDescription(
                "Refund", "Nonexistent Policy", "No refunds");

        assertFalse(foundPolicy.isPresent());
    }
}
