package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.*;
import org.studysync.studysync.constant.GroupMemberRole;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudyGroup group;

    @Column(nullable = false, length = 10)
    private GroupMemberRole role;


    public static Member createAdminMember(User user, StudyGroup group) {
        return Member.builder()
                .user(user)
                .group(group)
                .role(GroupMemberRole.ADMIN_ROLE)
                .build();
    }

    public static Member createCommonMember(User user, StudyGroup group) {
        return Member.builder()
                .user(user)
                .group(group)
                .role(GroupMemberRole.MEMBER_ROLE)
                .build();
    }
}