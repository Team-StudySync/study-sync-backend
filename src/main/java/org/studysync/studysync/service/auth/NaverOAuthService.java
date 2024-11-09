package org.studysync.studysync.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.dto.auth.oauth.naver.NaverUserInfoResponse;
import org.studysync.studysync.exception.HttpErrorException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NaverOAuthService {
    private final WebClient webClient;

    public NaverOAuthService(@Qualifier("naverWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public NaverUserInfoResponse getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/v1/nid/me")
                .header(
                        "Authorization",
                        accessToken
                )
                .retrieve()
                .onStatus(status -> status.value() == 401,
                        this::handle401Error)
                .onStatus(status -> status.value() == 403,
                        this::handle403Error)
                .bodyToMono(NaverUserInfoResponse.class)
                .block();
    }

    private Mono<Throwable> handle401Error(ClientResponse response) {
        return Mono.error(new HttpErrorException(HttpErrorCode.UnauthorizedNaverError));
    }

    private Mono<Throwable> handle403Error(ClientResponse response) {
        return Mono.error(new HttpErrorException(HttpErrorCode.ForbiddenNaverError));
    }
}
