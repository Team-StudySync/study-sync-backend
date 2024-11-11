package org.studysync.studysync.service.invitation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.constant.GroupMemberRole;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.StudyGroup;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.invitation.Invitation.InvitationDto;
import org.studysync.studysync.dto.invitation.InvitationAccept.InvitationAcceptDto;
import org.studysync.studysync.dto.invitation.InvitationInfo.InvitationInfoDto;
import org.studysync.studysync.exception.HttpErrorException;
import org.studysync.studysync.repository.MemberRepository;
import org.studysync.studysync.repository.StudyGroupRepository;
import org.studysync.studysync.service.redis.RedisService;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InvitationService {
    @Value("${invitation.ttl}")
    private int invitationTtl;

    private final StudyGroupRepository studyGroupRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    public InvitationDto inviteGroup(String groupUuid, User user) {
        StudyGroup foundStudyGroup = studyGroupRepository.findByUuid(groupUuid).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchStudyGroupError));
        Member foundMember = memberRepository.findByGroupAndUser(foundStudyGroup, user).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchStudyGroupMemberError));

        if(foundMember.getRole() != GroupMemberRole.ADMIN_ROLE){
            throw new HttpErrorException(HttpErrorCode.AccessDeniedError);
        }

        String code = UUID.randomUUID().toString();
        redisService.save(code, foundStudyGroup.getUuid(), Duration.ofMillis(invitationTtl));

        return InvitationDto.of(foundStudyGroup.getUuid(), code);
    }

    public InvitationInfoDto getInvitation(String code) {
        String foundGroupId = redisService.get(code).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchInvitationCodeError));
        StudyGroup foundStudyGroup = studyGroupRepository.findByUuid(foundGroupId).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchStudyGroupError));

        return InvitationInfoDto.fromEntity(foundStudyGroup);
    }

    public InvitationAcceptDto acceptInvitation(String code, User user) {
        String foundGroupId = redisService.get(code).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchInvitationCodeError));
        StudyGroup foundStudyGroup = studyGroupRepository.findByUuid(foundGroupId).orElseThrow(() -> new HttpErrorException(HttpErrorCode.NoSuchStudyGroupError));
        memberRepository.findByGroupAndUser(foundStudyGroup, user).ifPresent(foundMember -> { throw new HttpErrorException(HttpErrorCode.AlreadyExistMemberError); });

        Member savedMember = memberRepository.save(Member.createCommonMember(user, foundStudyGroup));

        return InvitationAcceptDto.fromEntity(savedMember);
    }
}
