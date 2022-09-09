package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterValidator.class)
@Documented
public @interface After {
    String message() default "date is not after";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int year() default 1;
    int month() default 1;
    int day() default 1;
}
