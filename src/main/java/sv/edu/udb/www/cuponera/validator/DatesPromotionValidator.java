package sv.edu.udb.www.cuponera.validator;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import sv.edu.udb.www.cuponera.entities.Promotions;

public class DatesPromotionValidator implements ConstraintValidator<DatesPromotionConstraint, Promotions>{
	@Override
    public void initialize(DatesPromotionConstraint constraint) {
    }
 
    @Override
    public boolean isValid(Promotions promotion, ConstraintValidatorContext context) {
    	LocalDate initDate = promotion.getInitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
    			endDate = promotion.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
    			limitDate = promotion.getLimitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	if(endDate.isAfter(initDate) && limitDate.isAfter(endDate)) {
    		return true;
    	}
        return false;
    }
}
