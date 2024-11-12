package org.studysync.studysync.service.group;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.StudyGroup;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.group.studyGroupCreate.StudyGroupCreateDto;
import org.studysync.studysync.dto.group.studyGroupCreate.StudyGroupCreateRequestDto;
import org.studysync.studysync.dto.group.studyGroupListGet.StudyGroupListGetDto;
import org.studysync.studysync.repository.StudyGroupRepository;
import org.studysync.studysync.repository.MemberRepository;

import java.util.List;

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

    public StudyGroupListGetDto getStudyGroups(User user) {
        List<Member> foundMembers = memberRepository.findByUser(user);
        List<StudyGroup> groups = foundMembers.stream().map(Member::getGroup).toList(); // 유저가 속한 그룹 리스트
        return StudyGroupListGetDto.fromEntity(groups);
    }
}
