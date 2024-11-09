package org.studysync.studysync.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    public UserInfo.Dto getUserInfo(User user) {
        return UserInfo.Dto.fromEntity(user);
    }
}
