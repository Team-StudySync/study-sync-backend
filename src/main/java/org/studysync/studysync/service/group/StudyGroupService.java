package org.studysync.studysync.service.group;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.StudyGroup;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.group.StudyGroupCreateDto;
import org.studysync.studysync.dto.group.StudyGroupCreateRequestDto;
import org.studysync.studysync.repository.StudyGroupRepository;
import org.studysync.studysync.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final MemberRepository memberRepository;

    public StudyGroupCreateDto createStudyGroup(User user, StudyGroupCreateRequestDto requestDto) {
        StudyGroup newGroup = studyGroupRepository.save(StudyGroup.of(
                requestDto.getGroupName(),
                requestDto.getStudyInfo(),
                requestDto.getStudyGoal()
        ));

        Member admin = memberRepository.save(Member.createAdminMember(user, newGroup));

        return StudyGroupCreateDto.of(admin);
    }
}
