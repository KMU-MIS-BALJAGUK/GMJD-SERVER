package org.baljaguk.domain.teamapplication.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamApplyRequest {

    private String comment;
    private String selectedSkills;
}
