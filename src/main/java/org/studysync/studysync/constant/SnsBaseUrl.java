package org.studysync.studysync.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsBaseUrl {
    KakaoBaseUrl("https://kapi.kakao.com"),
    NaverBaseUrl("https://openapi.naver.com"),
    TestBaseUrl("http://localhost");

    private final String url;
}
