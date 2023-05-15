package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import com.cnu.taleteller.backend.domain.tool.dto.ScenarioDto;
import com.cnu.taleteller.backend.domain.tool.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    @Transactional
    public List<Scenario> save(String[] scenario, Long bookId) {
        List<Scenario> scenarios = new ArrayList<>();

        // 도입
        Optional<Scenario> scenario1 = scenarioRepository.findByBook_BookIdAndScenarioType(bookId, "도입");
        if (scenario1.isPresent()) {
            Scenario existingScenario = scenario1.get();
            existingScenario.update(scenario[0], "도입");
            scenarios.add(existingScenario);
        } else {
            ScenarioDto dto = ScenarioDto.builder()
                    .scenarioContent(scenario[0])
                    .scenarioType("도입")
                    .book(new Book(bookId))
                    .build();
            scenarios.add(dto.toEntity());
        }

        // 전개
        Optional<Scenario> scenario2 = scenarioRepository.findByBook_BookIdAndScenarioType(bookId, "전개");
        if (scenario2.isPresent()) {
            Scenario existingScenario = scenario2.get();
            existingScenario.update(scenario[1], "전개");
            scenarios.add(existingScenario);
        } else {
            ScenarioDto dto = ScenarioDto.builder()
                    .scenarioContent(scenario[1])
                    .scenarioType("전개")
                    .book(new Book(bookId))
                    .build();
            scenarios.add(dto.toEntity());
        }

        // 위기
        Optional<Scenario> scenario3 = scenarioRepository.findByBook_BookIdAndScenarioType(bookId, "위기");
        if (scenario3.isPresent()) {
            Scenario existingScenario = scenario3.get();
            existingScenario.update(scenario[2], "위기");
            scenarios.add(existingScenario);
        } else {
            ScenarioDto dto = ScenarioDto.builder()
                    .scenarioContent(scenario[2])
                    .scenarioType("위기")
                    .book(new Book(bookId))
                    .build();
            scenarios.add(dto.toEntity());
        }

        // 결말
        Optional<Scenario> scenario4 = scenarioRepository.findByBook_BookIdAndScenarioType(bookId, "결말");
        if (scenario4.isPresent()) {
            Scenario existingScenario = scenario4.get();
            existingScenario.update(scenario[3], "결말");
            scenarios.add(existingScenario);
        } else {
            ScenarioDto dto = ScenarioDto.builder()
                    .scenarioContent(scenario[3])
                    .scenarioType("결말")
                    .book(new Book(bookId))
                    .build();
            scenarios.add(dto.toEntity());
        }

        List<Scenario> result = scenarioRepository.saveAll(scenarios);
        return result;
    }

}
