package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyTeamDetailResponse(
        String teamTitle,
        String contestName,
        String contestOrganizationName,
        Long memberCount,
        String myMemberType,   // 나의 팀원 타입
        List<MemberInfo> members
) {

    public static MyTeamDetailResponse of(String teamTitle,
                                          String contestName,
                                          String contestOrganizationName,
                                          Long memberCount,
                                          String myMemberType,
                                          List<MemberInfo> members) {
        return new MyTeamDetailResponse(
                teamTitle,
                contestName,
                contestOrganizationName,
                memberCount,
                myMemberType,
                members
        );
    }

    // 내부 DTO
    public record MemberInfo(
            Long memberId,
            String profileImageUrl,
            String name,
            String memberType
    ) {
        public static MemberInfo of(Long memberId,
                                    String profileImageUrl,
                                    String name,
                                    String memberType) {
            return new MemberInfo(memberId, profileImageUrl, name, memberType);
        }
    }
}
