package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.studysync.studysync.constant.SnsType;
import org.studysync.studysync.dto.auth.oauth.userInfo.OAuthUserInfoDto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User implements OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String snsId;

    @Column(nullable = false, length = 50)
    private SnsType snsType;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String profileImage;

    @Column(length = 255)
    private String email;

    private String authority;


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    @Override
    public String getName() {
        return name;
    }

    public static User from(OAuthUserInfoDto dto){
        return User.builder()
                .snsId(dto.getSnsId())
                .snsType(dto.getSnsType())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && id.equals(((User) obj).id);
    }
}
