package org.studysync.studysync.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroupMemberRole {
    ADMIN_ROLE("관리자"),
    MEMBER_ROLE("멤버");

    private final String roleName;
}
