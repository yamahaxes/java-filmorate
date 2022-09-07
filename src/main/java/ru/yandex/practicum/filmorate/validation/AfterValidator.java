package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {

    private LocalDate startDate;

    @Override
    public void initialize(After constraintAnnotation) {
        this.startDate = LocalDate.of(constraintAnnotation.year(),
                constraintAnnotation.month(),
                constraintAnnotation.day());

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(startDate) || date.isEqual(startDate);
    }
}
