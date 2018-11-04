package sv.edu.udb.www.cuponera.entities.simple;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.PromotionsState;
import sv.edu.udb.www.cuponera.entities.Sales;

public class SimplePromotions {
	
	private Integer id;
	private String title;
	private BigDecimal regularPrice;
	private BigDecimal ofertPrice;
	private String initDate;
	private String endDate;
	private String limitDate;
	private long limitCant;
	private String description;
	private String otherDetails;
	private String image;
	private int couponsSold;
	private long couponsAvailable;
	private BigDecimal earnings;
	private BigDecimal chargeService;
	private String idCompany;
	private String company;
	private int idCompanyType;
	private String companyType;
	private int idState;
	private String state;
	private String rejectedDescription;
	
	public SimplePromotions(Promotions promotion) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		this.id = promotion.getId();
		this.title = promotion.getTitle();
		this.regularPrice = promotion.getRegularPrice();
		this.ofertPrice = promotion.getOfertPrice();
		this.initDate = format.format(promotion.getInitDate());
		this.endDate = format.format(promotion.getEndDate());
		this.limitDate = format.format(promotion.getLimitDate());
		this.description = promotion.getDescription();
		this.limitCant = promotion.getLimitCant();
		this.otherDetails = promotion.getOtherDetails();
		this.image = promotion.getImage();
		this.couponsSold = promotion.getCouponsSold();
		this.couponsAvailable = promotion.getCouponsAvailable();
		this.earnings = promotion.getEarnings();
		this.chargeService = promotion.getChargeService();
		this.idCompany = promotion.getCompany().getId();
		this.company = promotion.getCompany().getName();
		this.idCompanyType = promotion.getCompany().getCompanyType().getId();
		this.companyType = promotion.getCompany().getCompanyType().getType();
		this.idState = promotion.getState().getId();
		this.state = promotion.getState().getState();
		this.rejectedDescription = promotion.getRejectedDescription();
	}
	
	public static List<SimplePromotions> Parse(List<Promotions> Promotions)
	{
		List<SimplePromotions> simples = new ArrayList<>();
		
		for(Promotions promo : Promotions) simples.add(new SimplePromotions(promo));
		
		return simples;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}

	public BigDecimal getOfertPrice() {
		return ofertPrice;
	}

	public void setOfertPrice(BigDecimal ofertPrice) {
		this.ofertPrice = ofertPrice;
	}

	public String getInitDate() {
		return initDate;
	}

	public void setInitDate(String initDate) {
		this.initDate = initDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public long getLimitCant() {
		return limitCant;
	}

	public void setLimitCant(long limitCant) {
		this.limitCant = limitCant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getCouponsSold() {
		return couponsSold;
	}

	public void setCouponsSold(int couponsSold) {
		this.couponsSold = couponsSold;
	}

	public Long getCouponsAvailable() {
		return couponsAvailable;
	}

	public void setCouponsAvailable(Long couponsAvailable) {
		this.couponsAvailable = couponsAvailable;
	}

	public BigDecimal getEarnings() {
		return earnings;
	}

	public void setEarnings(BigDecimal earnings) {
		this.earnings = earnings;
	}

	public BigDecimal getChargeService() {
		return chargeService;
	}

	public void setChargeService(BigDecimal chargeService) {
		this.chargeService = chargeService;
	}

	public String getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(String idCompany) {
		this.idCompany = idCompany;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getIdCompanyType() {
		return idCompanyType;
	}

	public void setIdCompanyType(int idCompanyType) {
		this.idCompanyType = idCompanyType;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRejectedDescription() {
		return rejectedDescription;
	}

	public void setRejectedDescription(String rejectedDescription) {
		this.rejectedDescription = rejectedDescription;
	}

	public int getIdState() {
		return idState;
	}

	public void setIdState(int idState) {
		this.idState = idState;
	}
	
}
