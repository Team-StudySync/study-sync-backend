package org.studysync.studysync.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.auth.login.LoginDto;
import org.studysync.studysync.dto.auth.login.LoginRequestDto;
import org.studysync.studysync.dto.auth.oauth.userInfo.OAuthUserInfoDto;
import org.studysync.studysync.dto.auth.tokenReissue.TokenReIssueDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.provider.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.studysync.studysync.repository.UserRepository;
import org.studysync.studysync.service.redis.RedisService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final KakaoOAuthService kakaoOAuthService;
    private final NaverOAuthService naverOAuthService;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public LoginDto login(LoginRequestDto requestDto) {
        OAuthUserInfoDto userInfo = getUserInfo(requestDto.getSnsType(), requestDto.getAccessToken());
        Optional<User> user = userRepository.findBySnsId(userInfo.getSnsId());
        if (user.isEmpty()) {
            signup(userInfo);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userInfo.getSnsId());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        redisService.save(refreshToken, accessToken);

        return LoginDto.of(accessToken, refreshToken);
    }


    private OAuthUserInfoDto getUserInfo(SnsType snsType, String accessToken) {
        return switch (snsType) {
            case Kakao -> OAuthUserInfoDto.from(kakaoOAuthService.getUserInfo(accessToken));
            case Naver -> OAuthUserInfoDto.from(naverOAuthService.getUserInfo(accessToken));
        };
    }

    private void signup(OAuthUserInfoDto userInfo) {
        userRepository.save(User.from(userInfo));
    }

    public void logout(String refreshToken) {
        String resolvedRefreshToken = jwtTokenProvider.resolveToken(refreshToken);

        Optional<String> savedAccessToken = redisService.get(resolvedRefreshToken);
        if (savedAccessToken.isEmpty()) {
            throw new HttpErrorException(HttpErrorCode.NoSuchRefreshTokenError);
        }

        redisService.delete(resolvedRefreshToken);
    }

    public TokenReIssueDto reIssueToken(String accessToken, String refreshToken) {
        String resolvedAccessToken = jwtTokenProvider.resolveToken(accessToken);
        String resolvedRefreshToken = jwtTokenProvider.resolveToken(refreshToken);

        String savedAccessToken = redisService.get(resolvedRefreshToken).orElseThrow(
                () -> new HttpErrorException(HttpErrorCode.NoSuchRefreshTokenError)
        );

        // RefreshToken 유효성 및 만료여부 확인
        boolean isExpiredRefreshToken = jwtTokenProvider.isExpiredToken(resolvedRefreshToken);
        if (isExpiredRefreshToken) {
            redisService.delete(resolvedAccessToken);
            throw new HttpErrorException(HttpErrorCode.ExpiredRefreshTokenError);
        }

        // RefreshToken이 탈취 당한 경우
        if (!resolvedAccessToken.equals(savedAccessToken)) {
            redisService.delete(resolvedAccessToken);
            throw new HttpErrorException(HttpErrorCode.NoSuchAccessTokenError);
        }

        // AccessToken 유효성 및 만료여부 확인
        boolean isExpiredAccessToken = jwtTokenProvider.isExpiredToken(resolvedAccessToken);
        if (!isExpiredAccessToken) {
            redisService.delete(resolvedRefreshToken);
            throw new HttpErrorException(HttpErrorCode.NotExpiredAccessTokenError);
        }

        // 토큰 재발행
        String reIssuedAccessToken = jwtTokenProvider.reIssueAccessToken(resolvedAccessToken);
        redisService.delete(resolvedAccessToken);
        redisService.save(resolvedRefreshToken, reIssuedAccessToken);
        return TokenReIssueDto.of(reIssuedAccessToken);

    }
}
