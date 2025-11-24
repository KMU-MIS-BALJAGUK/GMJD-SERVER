package org.baljaguk.domain.team.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateTeamMemoRequest(

        @NotNull String memo

) {}
