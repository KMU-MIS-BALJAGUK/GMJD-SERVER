package org.baljaguk.global.util.customAnnotaion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.baljaguk.global.util.validator.EducationFieldValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EducationFieldValidator.class)
public @interface ValidateEducationFields {

    String message() default "학력 정보에 맞지 않는 필드 값입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
