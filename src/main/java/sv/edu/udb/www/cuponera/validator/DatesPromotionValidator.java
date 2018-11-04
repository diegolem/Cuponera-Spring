package sv.edu.udb.www.cuponera.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DatesPromotionValidator implements ConstraintValidator<DatesPromotionConstraint, Object[]>{
	@Override
    public void initialize(DatesPromotionConstraint constraint) {
    }
 
    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
    	if(value == null || !(value[0] instanceof LocalDate) || !(value[1] instanceof LocalDate)) {
    		return false;
    	}
        return ( ((LocalDate) value[1]).isAfter((LocalDate) value[0]) );
    }
}
