package com.orderline.basic.annotation;

import com.orderline.basic.validation.MultipleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MultipleValidator.class})
public @interface Multiple {

    String message() default "{value}의 배수로 입력해주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value() default 1;
}
