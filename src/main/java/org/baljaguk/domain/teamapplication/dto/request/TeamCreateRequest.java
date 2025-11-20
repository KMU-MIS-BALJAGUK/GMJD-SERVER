package org.baljaguk.domain.team.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {

    @NotBlank(message = "팀 제목은 필수 값입니다.")
    private String title;

    private String introduction;

    @NotNull(message = "최대 인원은 필수 값입니다.")
    @Min(value = 2, message = "팀은 최소 2인 이상이어야 합니다.")
    private Integer maxMember;
}
