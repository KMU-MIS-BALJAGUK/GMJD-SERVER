package org.baljaguk.domain.user.dto.request;

import java.util.Set;

public record CategoryUpdateRequest(
        Set<Long> categoryIds
) {}
