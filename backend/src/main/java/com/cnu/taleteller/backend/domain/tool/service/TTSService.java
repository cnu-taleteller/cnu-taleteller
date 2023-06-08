package com.cnu.taleteller.backend.domain.tool.service;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.tool.dto.TTSDto;
import com.cnu.taleteller.backend.domain.tool.entity.UploadFile;
import com.cnu.taleteller.backend.domain.tool.repository.UploadFileRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TTSService {

    private final S3Service s3Service;
    private final UploadFileRepository uploadFileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String encodedFileName;

    public String getEncodedFileName() {
        return encodedFileName;
    }
    public String generateTTSUrl(TTSDto dto) throws IOException {
        String text = dto.getText();
        String language = dto.getLanguage();
        String voice = dto.getVoice();
        if (voice.equals("female")) {
            voice = "ko-KR-Standard-A";
        } else {
            voice = "ko-KR-Standard-C";
        }

        // Google Cloud 플랫폼 서비스 계정 키 파일 경로
        String serviceAccountKeyFile = "C:/Users/rhrjs/Downloads/taleteller-382100-b7cc202c3850.json";

        // Google Cloud 플랫폼 서비스 계정 인증
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyFile));

        // TTS 클라이언트 초기화
        try (TextToSpeechClient ttsClient = TextToSpeechClient.create(TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build())) {

            // TTS 요청 생성
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voiceSelection = VoiceSelectionParams.newBuilder().setLanguageCode(language).setName(voice).build();
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
            SynthesizeSpeechRequest request = SynthesizeSpeechRequest.newBuilder()
                    .setInput(input)
                    .setVoice(voiceSelection)
                    .setAudioConfig(audioConfig)
                    .build();

            // TTS 호출 및 음성 파일 생성
            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(request);
            ByteString audioContent = response.getAudioContent();

            // 음성 파일을 S3 서버에 업로드
            String audioFileName = "TTS.mp3";
            Map<String, Serializable> s3UploadResult = s3Service.getPreSignedUrl(audioFileName);
            encodedFileName = (String) s3UploadResult.get("encodedFileName");
            Serializable preSignedUrl = (s3UploadResult.get("preSignedUrl"));
            System.out.println(preSignedUrl);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(audioContent.toByteArray())) {
                s3Service.uploadFile(encodedFileName, inputStream);
            }

            // TTS 듣기 URL 생성
            String baseUrl = "https://translate.google.com/translate_tts?ie=UTF-8&tl=" + language + "&q=";
            String ttsUrl = baseUrl + text;

            return ttsUrl;
        }
    }
}