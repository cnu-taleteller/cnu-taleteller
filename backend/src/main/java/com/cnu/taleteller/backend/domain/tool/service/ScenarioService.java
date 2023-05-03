package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.tool.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
}
