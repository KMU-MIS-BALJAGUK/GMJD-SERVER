package org.baljaguk.domain.user.dto.request;

import java.util.List;

public record CategoryUpdateRequest(
        List<Long> categoryIds
) {}
