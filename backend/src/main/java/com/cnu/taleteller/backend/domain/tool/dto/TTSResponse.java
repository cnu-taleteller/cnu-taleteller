package com.cnu.taleteller.backend.domain.tool.dto;

public class TTSResponse {
    private String ttsUrl;
    private String encodedFileName;

    public TTSResponse(String ttsUrl, String encodedFileName) {
        this.ttsUrl = ttsUrl;
        this.encodedFileName = encodedFileName;
    }

    public String getTtsUrl() {
        return ttsUrl;
    }

    public void setTtsUrl(String ttsUrl) {
        this.ttsUrl = ttsUrl;
    }

    public String getEncodedFileName() {
        return encodedFileName;
    }

    public void setEncodedFileName(String encodedFileName) {
        this.encodedFileName = encodedFileName;
    }
}
