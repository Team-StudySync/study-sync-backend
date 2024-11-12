package org.studysync.studysync.dto.member.memberListGet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberListGetDto {
    private List<MemberInfo> members;

    public static MemberListGetDto from(List<MemberInfo> members){
        return MemberListGetDto.builder()
                .members(members)
                .build();
    }
}
