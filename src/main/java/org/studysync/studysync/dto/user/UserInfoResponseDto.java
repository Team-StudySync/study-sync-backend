package org.studysync.studysync.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponseDto {
    private String snsId;

    private SnsType snsType;

    private String name;

    private String profileImage;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserInfoResponseDto fromDto(UserInfoDto dto) {
        return UserInfoResponseDto.builder()
                .snsId(dto.getSnsId())
                .snsType(dto.getSnsType())
                .name(dto.getName())
                .profileImage(dto.getProfileImage())
                .email(dto.getEmail())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
