package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.tool.domain.Scenario;
import com.cnu.taleteller.backend.domain.tool.dto.CaptionRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.KeywordRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.ScenarioDto;
import com.cnu.taleteller.backend.domain.tool.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    @Value("${OPENAI_API_KEY}")
    private String OPENAI_API_KEY;
    private String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String generateScenario(KeywordRequestDto requestDto) {
        try {
            // 요청 데이터 생성
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content",
                    String.format("누가: %s," +
                                    "\n언제: %s," +
                                    "\n어디서: %s," +
                                    "\n사건: %s" +
                                    "\n라는 내용을 가진 동화책을 " +
                                    "'도입/전개/위기/결말' 로 나눠서 써줘." +
                                    "\n내용을 나눌 때 형식은\n[도입] 내용\n[전개] 내용\n[위기] 내용\n[결말] 내용\n형식으로 나눠서 700자 이내로 써줘.",
                            requestDto.getWho(), requestDto.getWhen(), requestDto.getWhere(), requestDto.getEvent()));
            messages.add(userMessage);
            requestData.put("messages", messages);

            String result = makeOpenAIRequest(requestData);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate scenario", e);
        }
    }

    public String generateFlow(CaptionRequestDto captionRequestDto) {
        String story = captionRequestDto.getStory();
        List<String> captions = captionRequestDto.getCaptions();

        String content = story + " 라는 내용을 가진 동화책을 만드려고 하는데,\n"
                + "초반 내용: " + captions.get(0) + ", " + captions.get(1) + ", " + captions.get(2) + ", ...\n"
                + "후반 내용: " + captions.get(captions.size() - 2) + ", " + captions.get(captions.size() - 1) + "...\n"
                + "까지 제작이 진행된 상황이라면,\n"
                + "현재 만들고 있는 내 동화책은\n"
                + "[도입], [전개], [위기], [결말] 중 어디까지 진행된 거고, 어떤 내용을 더 추가해야할까?\n"
                + "처음에 말한 동화책 내용이랑 내가 제작하고 있는 내용이 상관없는 얘기라면 상관없는 내용이라고 말해줘.";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "gpt-3.5-turbo");
        requestData.put("messages", List.of(Map.of("role", "user", "content", content)));

        String result = makeOpenAIRequest(requestData);
        return result;
    }

    private String makeOpenAIRequest(Map<String, Object> requestData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
        ResponseEntity<Map> responseEntity = new RestTemplate().postForEntity(OPENAI_API_URL, requestEntity, Map.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, String> message = (Map<String, String>) choice.get("message");
                String content = message.get("content");
                return content;
            }
        }
        return null;
    }

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
