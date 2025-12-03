package org.baljaguk.global.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.global.util.customAnnotaion.ValidateEducationFields;
import org.springframework.util.StringUtils;

public class EducationFieldValidator implements ConstraintValidator<ValidateEducationFields, UserUpdateRequest> {

    @Override
    public boolean isValid(UserUpdateRequest request, ConstraintValidatorContext context) {

        // 학력이 HighSchool이면 대학 관련 필드는 null/빈값 허용
        if (request.education() == Education.HIGH_SCHOOL) {
            return true; // 별도 검증 필요 없음
        }

        // 학력이 UNIVERSITY인 경우 아래 값 모두 반드시 필수
        boolean hasUniversityName = StringUtils.hasText(request.universityName());
        boolean hasDegree = request.recognizedDegree() != null;
        boolean hasMajor = StringUtils.hasText(request.major());

        if (hasUniversityName && hasDegree && hasMajor) {
            return true;
        }

        // 커스텀 에러 메시지 세부화
        context.disableDefaultConstraintViolation();

        if (!hasUniversityName) {
            context.buildConstraintViolationWithTemplate("대학교 이름은 필수입니다.")
                    .addPropertyNode("universityName")
                    .addConstraintViolation();
        }
        if (!hasDegree) {
            context.buildConstraintViolationWithTemplate("인정학력은 필수입니다.")
                    .addPropertyNode("recognizedDegree")
                    .addConstraintViolation();
        }
        if (!hasMajor) {
            context.buildConstraintViolationWithTemplate("전공명은 필수입니다.")
                    .addPropertyNode("major")
                    .addConstraintViolation();
        }

        return false;
    }
}
