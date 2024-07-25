package org.example.mrj.util;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DayMonthValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DayMonth {

    String message() default "Invalid date format. Expected format is DD:MM";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
