package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyTeamDetailResponse(
        String teamTitle,
        String contestName,
        String contestOrganizationName,
        Long memberCount,
        String myMemberType,   // 나의 팀원 타입
        String memo,
        List<MemberInfo> members,
        Long contestId,
        Long chatRoomId
) {

    public static MyTeamDetailResponse of(String teamTitle,
                                          String contestName,
                                          String contestOrganizationName,
                                          Long memberCount,
                                          String myMemberType,
                                          String memo,
                                          List<MemberInfo> members,
                                          Long contestId,
                                          Long chatRoomId) {
        return new MyTeamDetailResponse(
                teamTitle,
                contestName,
                contestOrganizationName,
                memberCount,
                myMemberType,
                memo,
                members,
                contestId,
                chatRoomId
        );
    }

    // 내부 DTO
    public record MemberInfo(
            Long userId,
            String profileImageUrl,
            String name,
            String memberType
    ) {
        public static MemberInfo of(Long userId,
                                    String profileImageUrl,
                                    String name,
                                    String memberType) {
            return new MemberInfo(userId, profileImageUrl, name, memberType);
        }
    }
}
