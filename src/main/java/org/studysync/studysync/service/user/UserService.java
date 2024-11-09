package org.studysync.studysync.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfoDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    public UserInfoDto getUserInfo(User user) {
        return UserInfoDto.fromEntity(user);
    }
}
