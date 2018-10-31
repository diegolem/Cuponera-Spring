package sv.edu.udb.www.cuponera.validator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PricesPromotionValidator.class})
public @interface PricesPromotionConstraint {
	String message() default "El precio de oferta debe ser menor al precio regular";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
