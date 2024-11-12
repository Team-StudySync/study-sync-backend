package org.studysync.studysync.dto.member.memberListGet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.constant.GroupMemberRole;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.User;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfo {
    @Schema(example = "멤버 아이디")
    private Long memberId;

    @Schema(example = "멤버 이름")
    private String memberName;

    @Schema(example = "멤버 권한")
    private GroupMemberRole memberRole;

    public static MemberInfo of(User user, Member member) {
        return MemberInfo.builder()
                .memberId(member.getId())
                .memberName(user.getName())
                .memberRole(member.getRole())
                .build();
    }
}
