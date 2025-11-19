package org.baljaguk.global.util.customAnnotaion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.baljaguk.global.util.validator.KeywordValidator;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KeywordValidator.class)
public @interface ValidKeyword {
    String message() default "검색어는 공백 제거 후 최소 2글자 이상이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
