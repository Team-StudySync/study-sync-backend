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
public class UserInfoDto {
    private String snsId;

    private SnsType snsType;

    private String name;

    private String profileImage;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserInfoDto fromEntity(User user) {
        return UserInfoDto.builder()
                .snsId(user.getSnsId())
                .snsType(user.getSnsType())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .email(user.getEmail())
                .build();
    }
}
