
package org.example.mrj.util;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayMonthValidator implements ConstraintValidator<DayMonth, String>
{

    @Override
    public void initialize(DayMonth constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Consider null values as valid. Handle null check in your business logic if necessary.
        }

        return value.matches("^\\d{2}:\\d{2}$") && isValidDayMonth(value);
    }

    private boolean isValidDayMonth(String value) {
        String[] parts = value.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        return (day >= 1 && day <= 31) && (month >= 1 && month <= 12);
    }
}
