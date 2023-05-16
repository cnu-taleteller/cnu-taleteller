package com.cnu.taleteller.backend.domain.tool.repository;

import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
