package org.studysync.studysync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.studysync.studysync.constant.SnsBaseUrl;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder()
                .baseUrl(SnsBaseUrl.KakaoBaseUrl.getUrl())
                .build();
    }

    @Bean
    public WebClient naverWebClient(){
        return WebClient.builder()
                .baseUrl(SnsBaseUrl.NaverBaseUrl.getUrl())
                .build();
    }
}
