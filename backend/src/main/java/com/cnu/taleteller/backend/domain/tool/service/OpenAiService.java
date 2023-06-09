package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.tool.dto.CaptionRequestDto;
import com.cnu.taleteller.backend.domain.tool.dto.KeywordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final BookRepository bookRepository;
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


}
