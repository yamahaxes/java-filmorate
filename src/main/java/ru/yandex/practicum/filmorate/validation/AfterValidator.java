package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AfterValidator implements ConstraintValidator<After, Date> {

    private LocalDate startDate;

    @Override
    public void initialize(After constraintAnnotation) {
        this.startDate = LocalDate.of(constraintAnnotation.year(),
                constraintAnnotation.month(),
                constraintAnnotation.day());

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        return !startDate.isAfter(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
}
