package org.studysync.studysync.service.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.exception.HttpErrorException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {
    @InjectMocks
    private RedisService redisService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Nested
    @DisplayName("레디스 저장")
    class RedisSave {
        @Test
        @DisplayName("성공")
        void success(){
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            redisService.save("key", "value");

            verify(redisTemplate, times(1)).opsForValue();
            verify(valueOperations, times(1)).set(any(), any());
        }
    }

    @Nested
    @DisplayName("레디스 조회")
    class RedisGet {
        @Test
        @DisplayName("key가 존재할 경우 value를 반환")
        void ExistKey_ReturnValue(){
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            given(valueOperations.get(any())).willReturn("value");

            Optional<String> result = redisService.get("key");

            Assertions.assertThat(result).isNotEmpty();
            Assertions.assertThat(result.get()).isEqualTo("value");

            verify(redisTemplate, times(1)).opsForValue();
            verify(valueOperations, times(1)).get(any());
        }

        @Test
        @DisplayName("key가 존재하지 않을 경우 빈 Optional을 반환")
        void NotExistKey_ReturnEmptyOptional(){
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            given(valueOperations.get(any())).willReturn(null);

            Optional<String> result = redisService.get("key");

            Assertions.assertThat(result).isEmpty();

            verify(redisTemplate, times(1)).opsForValue();
            verify(valueOperations, times(1)).get(any());
        }
    }

    @Nested
    @DisplayName("레디스 삭제")
    class RedisDelete {
        @Test
        @DisplayName("key가 존재할 경우 데이터를 삭제한다.")
        void ExistKey_DeleteValue(){
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            given(valueOperations.getAndDelete(any())).willReturn("value");

            redisService.delete("key");

            verify(redisTemplate, times(1)).opsForValue();
            verify(valueOperations, times(1)).getAndDelete(any());
        }


        @Test
        @DisplayName("key가 존재하지 않을 경우 오류 반환")
        void NotExistKey_ReturnError(){
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            given(valueOperations.getAndDelete(any())).willReturn(null);

            assertThatThrownBy(() -> redisService.delete("key"))
                    .isInstanceOf(HttpErrorException.class)
                    .hasMessageContaining(HttpErrorCode.InternalServerError.getMessage());

            verify(redisTemplate, times(1)).opsForValue();
            verify(valueOperations, times(1)).getAndDelete(any());
        }
    }
}