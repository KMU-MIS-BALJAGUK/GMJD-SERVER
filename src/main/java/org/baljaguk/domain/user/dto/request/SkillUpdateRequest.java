package org.baljaguk.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SkillUpdateRequest(
        List<String> skills
) {}
