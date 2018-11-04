package sv.edu.udb.www.cuponera.validator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Constraint(validatedBy = {DatesPromotionValidator.class})
public @interface DatesPromotionConstraint {
	String message() default "Valor de fechas no válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String initDate();
    String endDate();
}
