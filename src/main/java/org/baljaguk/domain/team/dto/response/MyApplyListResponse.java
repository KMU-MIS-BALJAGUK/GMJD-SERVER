package org.baljaguk.domain.team.dto.response;

import java.util.List;

public record MyApplyListResponse(
        List<MyApplyInfoResponse> myApplyList
) {
    public static MyApplyListResponse of(List<MyApplyInfoResponse> list) {
        return new MyApplyListResponse(list);
    }

    // 이너 DTO
    public record MyApplyInfoResponse(
            String contestImageUrl,
            String contestName,
            String teamTitle,
            Integer maxMember,
            Long memberCount,
            String status
    ) {
        public static MyApplyInfoResponse of(
                String contestImageUrl,
                String contestName,
                String teamTitle,
                Integer maxMember,
                Long memberCount,
                String status
        ) {
            return new MyApplyInfoResponse(
                    contestImageUrl,
                    contestName,
                    teamTitle,
                    maxMember,
                    memberCount,
                    status
            );
        }
    }
}
