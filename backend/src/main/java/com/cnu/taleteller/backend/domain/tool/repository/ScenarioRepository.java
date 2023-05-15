package com.cnu.taleteller.backend.domain.tool.repository;

import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    Optional<Scenario> findByBook_BookIdAndScenarioType(Long bookId, String type);
}
