package org.studysync.studysync.service.temp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.temp.login.TestLoginDto;
import org.studysync.studysync.dto.temp.login.TestLoginRequestDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.provider.JwtTokenProvider;
import org.studysync.studysync.repository.UserRepository;
import org.studysync.studysync.service.redis.RedisService;

@Service
@RequiredArgsConstructor
@Transactional
public class TempService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public TestLoginDto loginTestUser(TestLoginRequestDto requestDto) {
        User foundTestUser = userRepository.findById(requestDto.getTestUserId()).orElseThrow(() -> new HttpErrorException(HttpErrorCode.UserNotFoundError));

        String accessToken = jwtTokenProvider.generateAccessToken(foundTestUser.getSnsId());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        redisService.save(refreshToken, accessToken);

        return TestLoginDto.of(accessToken, refreshToken);
    }
}
