package org.studysync.studysync.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;

import java.time.LocalDateTime;

public class UserInfo {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Dto {
        private String snsId;

        private SnsType snsType;

        private String nickname;

        private String email;

        private Double weight;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public static Dto fromEntity(User user) {
            return Dto.builder()
                    .snsId(user.getSnsId())
                    .snsType(user.getSnsType())
                    .email(user.getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String snsId;

        private SnsType snsType;

        private String nickname;

        private String email;

        private Double weight;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public static Response fromDto(Dto dto) {
            return Response.builder()
                    .snsId(dto.getSnsId())
                    .snsType(dto.getSnsType())
                    .nickname(dto.getNickname())
                    .email(dto.getEmail())
                    .weight(dto.getWeight())
                    .createdAt(dto.getCreatedAt())
                    .updatedAt(dto.getUpdatedAt())
                    .build();
        }
    }

}
