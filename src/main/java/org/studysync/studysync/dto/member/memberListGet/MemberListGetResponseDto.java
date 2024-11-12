package org.studysync.studysync.dto.member.memberListGet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberListGetResponseDto {
    private List<MemberInfo> members;

    public static MemberListGetResponseDto fromEntity(MemberListGetDto dto){
        return MemberListGetResponseDto.builder()
                .members(dto.getMembers())
                .build();
    }
}
