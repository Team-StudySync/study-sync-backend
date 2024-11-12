package org.studysync.studysync.service.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.StudyGroup;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.member.memberListGet.MemberInfo;
import org.studysync.studysync.dto.member.memberListGet.MemberListGetDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.repository.MemberRepository;
import org.studysync.studysync.repository.StudyGroupRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final StudyGroupRepository studyGroupRepository;
    private final MemberRepository memberRepository;

    public MemberListGetDto getMembers(String groupId, User user) {
        StudyGroup foundStudyGroup = studyGroupRepository.findByUuid(groupId).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchStudyGroupError));
        List<Member> foundMembers = memberRepository.findByGroup(foundStudyGroup);

        boolean hasSameUser = false; // 요청을 보낸 유저가 그룹의 멤버인지 체크하는 플래그
        List<MemberInfo> memberInfos = new ArrayList<>();
        for (Member member : foundMembers) {
            User memberUser = member.getUser();
            if (memberUser.equals(user)) {
                hasSameUser = true;
            }
            memberInfos.add(MemberInfo.of(memberUser, member));
        }

        if(!hasSameUser) {
            throw new HttpErrorException(HttpErrorCode.NoSuchStudyGroupMemberError);
        }

        return MemberListGetDto.from(memberInfos);
    }
}
