package org.studysync.studysync.service.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.auth.login.LoginDto;
import org.studysync.studysync.dto.auth.login.LoginRequestDto;
import org.studysync.studysync.dto.auth.oauth.kakao.KakaoAccount;
import org.studysync.studysync.dto.auth.oauth.kakao.KakaoUserInfoResponse;
import org.studysync.studysync.dto.auth.oauth.kakao.KakaoUserProfile;
import org.studysync.studysync.dto.auth.tokenReissue.TokenReIssueDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.provider.JwtTokenProvider;
import org.studysync.studysync.repository.UserRepository;
import org.studysync.studysync.service.redis.RedisService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private KakaoOAuthService kakaoOAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    @Nested
    @DisplayName("로그인")
    class Login {
        String kakaoAccessToken;
        SnsType kakaoSnsType;
        String snsId;
        LoginRequestDto requestDto;
        KakaoUserInfoResponse response;

        @BeforeEach
        void setUp() {
            kakaoAccessToken = "kakaoAccessToken";
            kakaoSnsType = SnsType.Kakao;
            snsId = "snsId";
            requestDto = LoginRequestDto.builder()
                    .accessToken(kakaoAccessToken)
                    .snsType(kakaoSnsType)
                    .build();

            response = new KakaoUserInfoResponse();
            response.setId(1L);
            KakaoUserProfile userProfile = KakaoUserProfile.builder().nickname("nickname").build();
            response.setKakaoAccount(
                    KakaoAccount
                            .builder()
                            .profile(userProfile)
                            .build());
        }
        
        
        @Test
        @DisplayName("가입한 적이 있는 유저일 경우 토큰 반환")
        void existUser_ReturnToken() {
            User user = new User(1L, snsId, kakaoSnsType, "name", "profileImage", "email", "");

            // given
            given(kakaoOAuthService.getUserInfo(any())).willReturn(response);
            given(userRepository.findBySnsId(any())).willReturn(Optional.of(user));
            given(jwtTokenProvider.generateAccessToken(any())).willReturn("testAccessToken");
            given(jwtTokenProvider.generateRefreshToken()).willReturn("testRefreshToken");

            // when
            LoginDto result = authService.login(requestDto);

            // then
            Assertions.assertThat(result.getToken().getAccessToken()).isEqualTo("testAccessToken");
            Assertions.assertThat(result.getToken().getRefreshToken()).isEqualTo("testRefreshToken");
        }

        @Test
        @DisplayName("가입한 적이 없는 유저일 경우 하면 토큰 반환")
        void unknownUser_ReturnToken() {
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .accessToken(kakaoAccessToken)
                    .snsType(kakaoSnsType)
                    .build();

            given(kakaoOAuthService.getUserInfo(any())).willReturn(response);
            given(userRepository.findBySnsId(any())).willReturn(Optional.empty());
            given(jwtTokenProvider.generateAccessToken(any())).willReturn("testAccessToken");
            given(jwtTokenProvider.generateRefreshToken()).willReturn("testRefreshToken");

            // when
            LoginDto result = authService.login(requestDto);

            // then
            Assertions.assertThat(result.getToken().getAccessToken()).isEqualTo("testAccessToken");
            Assertions.assertThat(result.getToken().getRefreshToken()).isEqualTo("testRefreshToken");
        }

        @Test
        @DisplayName("oauth 서버에 오류가 있는 경우 오류 반환")
        void oAuthServerError_ReturnError() {
            HttpErrorException unauthorizedKakaoErrorException = new HttpErrorException(HttpErrorCode.UnauthorizedKakaoError);

            //given
            given(kakaoOAuthService.getUserInfo(any())).willThrow(unauthorizedKakaoErrorException);

            // when
            Throwable thrown = catchThrowable(() -> authService.login(requestDto));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(unauthorizedKakaoErrorException.getMessage());
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class Logout{
        String accessToken;
        String refreshToken;
        String resolvedAccessToken;
        String resolvedRefreshToken;
        String reIssuedAccessToken;

        @BeforeEach
        void setUp() {
            accessToken = "testAccessToken";
            refreshToken = "testRefreshToken";
            resolvedAccessToken = "testResolvedAccessToken";
            resolvedRefreshToken = "testResolvedRefreshToken";
            reIssuedAccessToken = "testReIssuedAccessToken";
        }


        @Test
        @DisplayName("성공")
        void validRefreshToken_DeleteUserToken() {
            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.of(accessToken));

            // when
            authService.logout(refreshToken);

            // then
            verify(jwtTokenProvider, times(1)).resolveToken(refreshToken);
            verify(redisService, times(1)).get(resolvedRefreshToken);
            verify(redisService, times(1)).delete(resolvedRefreshToken);
        }

        @Test
        @DisplayName("존재하지 않은 refreshToken일 경우 오류 반환")
        void unknownRefreshToken_ReturnError() {
            HttpErrorException noSuchRefreshErrorException = new HttpErrorException(HttpErrorCode.NoSuchRefreshTokenError);

            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.empty());

            // when
            Throwable thrown = catchThrowable(() -> authService.logout(refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(noSuchRefreshErrorException.getMessage());
            verify(jwtTokenProvider, times(1)).resolveToken(refreshToken);
            verify(redisService, times(1)).get(resolvedRefreshToken);
        }

        @Test
        @DisplayName("유효하지 않은 refreshToken일 경우  오류 반환")
        void InvalidRefreshToken_ReturnError() {
            HttpErrorException notValidTokenErrorException = new HttpErrorException(HttpErrorCode.NotValidTokenError);

            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willThrow(notValidTokenErrorException);

            // when
            Throwable thrown = catchThrowable(() -> authService.logout(refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(notValidTokenErrorException.getMessage());
            verify(jwtTokenProvider, times(1)).resolveToken(refreshToken);
        }
    }

    @Nested
    @DisplayName("토큰 재발급")
    class ReIssueToken {

        private String accessToken;
        private String refreshToken;
        private String resolvedAccessToken;
        private String resolvedRefreshToken;
        private String reIssuedAccessToken;

        @BeforeEach
        void setUp() {
            accessToken = "testAccessToken";
            refreshToken = "testRefreshToken";
            resolvedAccessToken = "testResolvedAccessToken";
            resolvedRefreshToken = "testResolvedRefreshToken";
            reIssuedAccessToken = "testReIssuedAccessToken";
        }

        @Test
        @DisplayName("성공")
        void validAccessTokenAndRefreshToken_ReturnReissuedAccessToken() {
            // given
            given(jwtTokenProvider.resolveToken(accessToken)).willReturn(resolvedAccessToken);
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.of(resolvedAccessToken));
            given(jwtTokenProvider.isExpiredToken(resolvedRefreshToken)).willReturn(false);
            given(jwtTokenProvider.isExpiredToken(resolvedAccessToken)).willReturn(true);
            given(jwtTokenProvider.reIssueAccessToken(resolvedAccessToken)).willReturn(reIssuedAccessToken);

            // when
            TokenReIssueDto result = authService.reIssueToken(accessToken, refreshToken);

            // then
            assertThat(result.getAccessToken()).isEqualTo(reIssuedAccessToken);
        }

        @Test
        @DisplayName("존재하지 않는 RefreshToken일 경우 오류 반환")
        void unknownRefreshToken_ReturnError() {
            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(jwtTokenProvider.resolveToken(accessToken)).willReturn(resolvedAccessToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.empty());

            // when
            Throwable thrown = catchThrowable(() -> authService.reIssueToken(accessToken, refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(HttpErrorCode.NoSuchRefreshTokenError.getMessage());
        }

        @Test
        @DisplayName("만료된 RefreshToken일 경우 오류 반환")
        void expiredRefreshToken_ReturnError() {
            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(jwtTokenProvider.resolveToken(accessToken)).willReturn(resolvedAccessToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.of(resolvedAccessToken));
            given(jwtTokenProvider.isExpiredToken(resolvedRefreshToken)).willReturn(true);

            // when
            Throwable thrown = catchThrowable(() -> authService.reIssueToken(accessToken, refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(HttpErrorCode.ExpiredRefreshTokenError.getMessage());
        }

        @Test
        @DisplayName("실제 저장된 값과 다른 AccessToken일 경우 오류 반환")
        void differentAccessToken_ReturnError() {
            // given
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(jwtTokenProvider.resolveToken(accessToken)).willReturn(resolvedAccessToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.of("differentAccessToken"));
            given(jwtTokenProvider.isExpiredToken(resolvedRefreshToken)).willReturn(false);

            // when
            Throwable thrown = catchThrowable(() -> authService.reIssueToken(accessToken, refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(HttpErrorCode.NoSuchAccessTokenError.getMessage());
        }

        @Test
        @DisplayName("만료되지 않은 AccessToken일 경우 오류 반환")
        void notExpiredAccessToken_ReturnError() {
            // given
            given(jwtTokenProvider.resolveToken(accessToken)).willReturn(resolvedAccessToken);
            given(jwtTokenProvider.resolveToken(refreshToken)).willReturn(resolvedRefreshToken);
            given(redisService.get(resolvedRefreshToken)).willReturn(Optional.of(resolvedAccessToken));
            given(jwtTokenProvider.isExpiredToken(resolvedRefreshToken)).willReturn(false);
            given(jwtTokenProvider.isExpiredToken(resolvedAccessToken)).willReturn(false);


            // when
            Throwable thrown = catchThrowable(() -> authService.reIssueToken(accessToken, refreshToken));

            // then
            assertThat(thrown)
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessage(HttpErrorCode.NotExpiredAccessTokenError.getMessage());
        }
    }

}