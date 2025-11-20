package org.baljaguk.domain.teamapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamApplyResponse {

    private boolean success;
    private String message;
    private Long applicationId;
}
