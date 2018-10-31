package sv.edu.udb.www.cuponera.validator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DatesPromotionValidator.class})
public @interface DatesPromotionConstraint {
	String message() default "Algunas fechas pueden no tener valores válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
