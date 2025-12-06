package org.baljaguk.domain.team.dto.response;

import org.baljaguk.domain.team.entity.RegisterStatus;

import java.util.Arrays;
import java.util.List;

public record TeamApplicantListResponse(
        Long teamId,
        List<ApplicantInfo> applicants
) {

    public static TeamApplicantListResponse of(Long teamId, List<ApplicantInfo> applicants) {
        return new TeamApplicantListResponse(teamId, applicants);
    }

    public record ApplicantInfo(
            Long userId,
            String profileImageUrl,
            String name,
            List<String> aiTags,
            RegisterStatus status
    ) {
        public static ApplicantInfo of(Long userId, String profileImageUrl, String name, String aiTagsCsv, RegisterStatus registerStatus) {

            // aiTagsCsv가 null이거나 빈 문자열이면 빈 리스트 반환
            List<String> tags = (aiTagsCsv == null || aiTagsCsv.isBlank())
                    ? List.of()
                    : Arrays.stream(aiTagsCsv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())  // 공백 요소 제거
                    .toList();

            return new ApplicantInfo(userId, profileImageUrl, name, tags, registerStatus);
        }
    }
}
