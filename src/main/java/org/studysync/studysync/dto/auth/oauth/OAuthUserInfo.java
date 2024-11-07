package org.studysync.studysync.dto.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.dto.auth.oauth.kakao.KakaoUserInfo;
import org.studysync.studysync.dto.auth.oauth.naver.NaverUserInfo;

public class OAuthUserInfo {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Dto {
        private String snsId;
        private SnsType snsType;
        private String name;
        private String email;

        // TODO: 카카오 검수 완료 후 회원가입 시 이메일 정보도 저장하도록 구현 필요
        public static Dto from(KakaoUserInfo.Response kakaoResponse) {
            return Dto.builder()
                    .snsId(String.valueOf(kakaoResponse.getId()))
                    .snsType(SnsType.Kakao)
                    .name(kakaoResponse.getKakaoAccount().getProfile().getNickname())
                    .email(null)
                    .build();
        }

        public static Dto from(NaverUserInfo.Response naverResponse) {
            return Dto.builder()
                    .snsId(naverResponse.getResponseDetails().getId())
                    .snsType(SnsType.Naver)
                    .name(naverResponse.getResponseDetails().getName())
                    .email(naverResponse.getResponseDetails().getEmail())
                    .build();
        }

        public static Dto of(String snsId, SnsType snsType, String name, String email) {
            return Dto.builder()
                    .snsId(snsId)
                    .snsType(snsType)
                    .name(name)
                    .email(email)
                    .build();
        }
    }
}
