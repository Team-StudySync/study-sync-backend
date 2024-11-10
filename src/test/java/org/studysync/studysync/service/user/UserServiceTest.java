package org.studysync.studysync.service.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfoDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("회원 정보 조회")
    class UserInfoGet {
        private static User user;

        @BeforeAll
        static void setUp() {
            user = new User(1L, "snsId", SnsType.Kakao, "최민수", "profileImage", "email", "");
        }

        @Test
        @DisplayName("성공")
        void success(){
            UserInfoDto userInfo = userService.getUserInfo(user);

            Assertions.assertThat(userInfo).isNotNull();
            Assertions.assertThat(userInfo.getSnsId()).isEqualTo(user.getSnsId());
            Assertions.assertThat(userInfo.getSnsType()).isEqualTo(user.getSnsType());
            Assertions.assertThat(userInfo.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userInfo.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userInfo.getProfileImage()).isEqualTo(user.getProfileImage());
        }
    }

}