package org.baljaguk.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "유저 한줄소개 수정 요청 DTO")
public record IntroductionUpdateRequest(

        @Schema(description = "수정할 한줄소개", example = "꾸준히 성장하는 개발자입니다!")
        @NotBlank(message = "한줄소개는 비어 있을 수 없습니다.")
        String introduction
) {}
