package org.baljaguk.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.team.entity.TeamMember;
import org.baljaguk.domain.team.entity.TeamRole;
import org.baljaguk.domain.teamapplication.entity.TeamApplication;
import org.baljaguk.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@AllArgsConstructor
public class TeamDetailResponse {

    // 상단 정보
    private Long teamId;
    private Long contestId;
    private String contestName;
    private String title;
    private String description;
    private boolean recruiting;
    private int maxMemberCount;
    private int currentMemberCount;
    private int applyCount;


    private boolean isLeader;
    private boolean isMember;
    private boolean isApplied;

    // 팀원 목록
    @Singular("member")
    private List<MemberDto> members;

    // 지원자 목록
    @Singular("applier")
    private List<ApplierDto> appliers;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberDto {
        private Long userId;
        private String name;
        private String profileImageUrl;
        private String role;
        private boolean isMe;
        private boolean canKick;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ApplierDto {
        private Long userId;
        private String name;
        private String profileImageUrl;

        private String skillsSummary;
        private String answersSummary;
    }


    public static TeamDetailResponse of(
            Team team,
            User currentUser,
            List<TeamMember> teamMembers,
            List<TeamApplication> applications
    ) {

        Long currentUserId = currentUser.getId();

        boolean isLeader = team.getLeader().getId().equals(currentUserId);
        boolean isMember = teamMembers.stream()
                .anyMatch(m -> m.getUser().getId().equals(currentUserId));
        boolean isApplied = applications.stream()
                .anyMatch(a -> a.getUser().getId().equals(currentUserId));

        List<MemberDto> memberDtos = teamMembers.stream()
                .map(member -> {
                    User user = member.getUser();
                    boolean me = user.getId().equals(currentUserId);
                    boolean canKick = isLeader && !me && member.getRole() != TeamRole.LEADER;

                    return MemberDto.builder()
                            .userId(user.getId())
                            .name(user.getNickname())
                            .profileImageUrl(user.getProfileImageUrl())
                            .role(member.getRole().name())
                            .isMe(me)
                            .canKick(canKick)
                            .build();
                })
                .collect(Collectors.toList());

        List<ApplierDto> applierDtos = applications.stream()
                .map(app -> {
                    User user = app.getUser();
                    return ApplierDto.builder()
                            .userId(user.getId())
                            .name(user.getNickname())
                            .profileImageUrl(user.getProfileImageUrl())
                            // skillsSummary / answersSummary 는 엔티티에 맞게 나중에 채워도 된다.
                            .skillsSummary(null)
                            .answersSummary(null)
                            .build();
                })
                .collect(Collectors.toList());

        return TeamDetailResponse.builder()
                .teamId(team.getId())
                .contestId(team.getContest().getId())
                .contestName(team.getContest().getTitle()) // 엔티티 필드에 맞게 수정
                .title(team.getTitle())
                .description(team.getDescription())
                .recruiting(team.isRecruiting())
                .maxMemberCount(team.getMaxMemberCount())
                .currentMemberCount(teamMembers.size())
                .applyCount(applications.size())
                .isLeader(isLeader)
                .isMember(isMember)
                .isApplied(isApplied)
                .members(memberDtos)
                .appliers(applierDtos)
                .build();
    }
}
