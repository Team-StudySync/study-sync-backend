package org.studysync.studysync.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfo;
import org.studysync.studysync.exception.HttpErrorException;
import org.springframework.stereotype.Service;
import org.studysync.studysync.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserInfo.Dto getUserInfo(User user) {
        if(user == null) {
            throw new HttpErrorException(HttpErrorCode.UserNotFoundError);
        }

        return UserInfo.Dto.fromEntity(user);
    }
}
