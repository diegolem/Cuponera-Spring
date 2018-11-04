package sv.edu.udb.www.cuponera.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import sv.edu.udb.www.cuponera.entities.Promotions;

public class PricesPromotionValidator implements ConstraintValidator<PricesPromotionConstraint, Promotions>{
	@Override
    public void initialize(PricesPromotionConstraint constraint) {
    }
 
    @Override
    public boolean isValid(Promotions promotion, ConstraintValidatorContext context) {
    	if(promotion.getRegularPrice() !=  null && promotion.getOfertPrice() != null) {
    		return promotion.getRegularPrice().compareTo(promotion.getOfertPrice()) < 0;
    	}
    	return false;
    }
}
