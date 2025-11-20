package org.baljaguk.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TeamApplyStatusResponse {

    private boolean canApply;
    private String reasonCode;
    private String message;      // 화면에 바로 보여줄 한 줄 문구
}
