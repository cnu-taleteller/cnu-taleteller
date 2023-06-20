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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TTSService {

    private final S3Service s3Service;
    private final UploadFileRepository uploadFileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${TTS_CLIENT_ID}")
    private String clientId;

    @Value("${TTS_CLIENT_PW}")
    private String clientSecret;

    private String encodedFileName;

    public String getEncodedFileName() {
        return encodedFileName;
    }
    public String generateTTSUrl(TTSDto dto) throws IOException {
        String text = dto.getText();
        String voice = dto.getVoice();
        System.out.println(text);

        try {
            text = URLEncoder.encode(text, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            // post request
            String postParams = "speaker="+voice+"&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 mp3 파일 생성
                String tempname = Long.valueOf(new Date().getTime()).toString();
                String ttsUrl = "TTS"+tempname+".mp3";
                File f = new File(tempname + ".mp3");
                // S3 서버에 업로드
                Map<String, Serializable> s3UploadResult = s3Service.getPreSignedUrl(ttsUrl);
                encodedFileName = (String) s3UploadResult.get("encodedFileName");
                s3Service.uploadFile(encodedFileName, is);

                return ttsUrl;
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    return null;

        }
}