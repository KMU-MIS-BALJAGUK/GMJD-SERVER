package org.baljaguk.global.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.baljaguk.global.util.customAnnotaion.ValidKeyword;

public class KeywordValidator implements ConstraintValidator<ValidKeyword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        // 모든 공백 제거
        String cleaned = value.replaceAll("\\s+", "");

        // 최소 길이 2 체크
        return cleaned.length() >= 2;
    }
}
