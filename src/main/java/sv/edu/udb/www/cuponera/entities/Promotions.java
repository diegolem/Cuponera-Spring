package sv.edu.udb.www.cuponera.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

import sv.edu.udb.www.cuponera.validator.DatesPromotionConstraint;
import sv.edu.udb.www.cuponera.validator.PricesPromotionConstraint;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="promotion", catalog="cuponera")
public class Promotions implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Positive
	private Integer id;
	@Pattern(regexp="^([A-Z]|[a-z]|[ñÑ])[a-zA-Z ñÑáéíóú,0-9.#-]*$",message="Ingrese un titulo de oferta válido")
	@NotBlank(message="El titulo de la oferta es obligatorio")
	private String title;
	@Positive(message = "Precio regular debe ser mayor a 0")
	@NotNull(message="El precio regular es obligatorio")
	@Min(value = 1, message="Ingrese un precio mayor a 0")
	private BigDecimal regularPrice;
	@Positive(message = "Precio de oferta debe ser mayor a 0")
	@NotNull(message="El precio de la oferta es obligatorio")
	@Min(value = 1, message="Ingrese un precio mayor a 0")
	// @PricesPromotionConstraint
	private BigDecimal ofertPrice;
	@FutureOrPresent(message="La fecha de inicio debe ser igual o mayor a la actual")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="La fecha de inicio es obligatoria")
	private Date initDate;
	@Future(message="La fecha debe ser mayor a la actual")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="La fecha de finalización es obligatoria")
	@DatesPromotionConstraint(initDate = "initDate", endDate ="endDate")
	private Date endDate;
	@Future(message="La fecha debe ser mayor a la actual")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="La fecha limite es obligatoria")
	private Date limitDate;
	@PositiveOrZero(message="la cantidad limite debe ser mayor o igual que 0")
	private int limitCant;
	@Pattern(regexp="^([A-Z]|[a-z]|[ñÑ])[a-zA-Z ñÑáéíóú,0-9.#-]*$",message="Ingrese caracteres válidos en la descripción")
	@NotBlank(message="La descripcion es obligatoria")
	private String description;
	@Pattern(regexp="^([A-Z]|[a-z]|[ñÑ])[a-zA-Z ñÑáéíóú,0-9.#-]*$",message="Ingrese caracteres válidos en la descripción")
	@NotBlank(message="La descripcion es obligatoria")
	private String otherDetails;
	// @Pattern(regexp="^[a-zA-Z ñÑáéíóú,0-9.#-]{1}[a-zA-Z ñÑáéíóú,0-9.#-]*.[([jpg]|[png]|[jpeg]|[gif])]$",message="Ingrese un nombre de archivo valido")
	// @NotNull(message="La imagen es obligatoria")
	private String image;
	private int couponsSold;
	private int couponsAvailable;
	private BigDecimal earnings;
	private BigDecimal chargeService;
	private Companies company;
	private PromotionsState state;
	// @Pattern(regexp="^([A-Z]|[a-z]|[ñÑ])[a-zA-Z ñÑáéíóú,0-9.#-]*$",message="Ingrese una descripcion válida")
	private String rejectedDescription;
	private Set<Sales> sales = new HashSet<Sales>(0);
	
	public Promotions() {}
	
	public Promotions(Integer id, String title, BigDecimal regularPrice, BigDecimal ofertPrice, Date initDate, Date endDate,
			Date limitDate, int limitCant, String description, String otherDetails, String image, int couponsSold,
			int couponsAvailable, BigDecimal earnings, BigDecimal chargeService, Companies company,
			PromotionsState state, String rejectedDescription) {
		this.id = id;
		this.title = title;
		this.regularPrice = regularPrice;
		this.ofertPrice = ofertPrice;
		this.initDate = initDate;
		this.endDate = endDate;
		this.limitDate = limitDate;
		this.limitCant = limitCant;
		this.description = description;
		this.otherDetails = otherDetails;
		this.image = image;
		this.couponsSold = couponsSold;
		this.couponsAvailable = couponsAvailable;
		this.earnings = earnings;
		this.chargeService = chargeService;
		this.company = company;
		this.state = state;
		this.rejectedDescription = rejectedDescription;
	}
	

	public Promotions(Integer id, String title, BigDecimal regularPrice, BigDecimal ofertPrice, Date initDate, Date endDate,
			Date limitDate, int limitCant, String description, String otherDetails, String image, int couponsSold,
			int couponsAvailable, BigDecimal earnings, BigDecimal chargeService, Companies company,
			PromotionsState state, String rejectedDescription, Set<Sales> sales) {
		this.id = id;
		this.title = title;
		this.regularPrice = regularPrice;
		this.ofertPrice = ofertPrice;
		this.initDate = initDate;
		this.endDate = endDate;
		this.limitDate = limitDate;
		this.limitCant = limitCant;
		this.description = description;
		this.otherDetails = otherDetails;
		this.image = image;
		this.couponsSold = couponsSold;
		this.couponsAvailable = couponsAvailable;
		this.earnings = earnings;
		this.chargeService = chargeService;
		this.company = company;
		this.state = state;
		this.rejectedDescription = rejectedDescription;
		this.sales = sales;
	}
	
	@Id
	@GeneratedValue(strategy=IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="title", nullable=false, length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="regular_price", nullable=false, precision=18, scale=2)
	public BigDecimal getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}

	@Column(name="ofert_price", nullable=false, precision=18, scale=2)
	public BigDecimal getOfertPrice() {
		return ofertPrice;
	}
	public void setOfertPrice(BigDecimal ofertPrice) {
		this.ofertPrice = ofertPrice;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="init_date", nullable=false)
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="end_date", nullable=false)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="limit_date", nullable=false)
	public Date getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}

	@Column(name="limit_cant", nullable=false)
	public int getLimitCant() {
		return limitCant;
	}
	public void setLimitCant(int limitCant) {
		this.limitCant = limitCant;
	}

	@Column(name="description", nullable=false, length=500)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="other_details", nullable=false, length=500)
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	@Column(name="image", nullable=false, length=75)
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	@Column(name="coupons_sold", nullable=false)
	public int getCouponsSold() {
		return couponsSold;
	}
	public void setCouponsSold(int couponsSold) {
		this.couponsSold = couponsSold;
	}

	@Column(name="coupons_available", nullable=false)
	public int getCouponsAvailable() {
		return couponsAvailable;
	}
	public void setCouponsAvailable(int couponsAvailable) {
		this.couponsAvailable = couponsAvailable;
	}

	@Column(name="earnings", nullable=false, precision=18)
	public BigDecimal getEarnings() {
		return earnings;
	}
	public void setEarnings(BigDecimal earnings) {
		this.earnings = earnings;
	}

	@Column(name="charge_service", nullable=false, precision=18)
	public BigDecimal getChargeService() {
		return chargeService;
	}
	public void setChargeService(BigDecimal chargeService) {
		this.chargeService = chargeService;
	}

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_company", nullable=false)
	public Companies getCompany() {
		return company;
	}
	public void setCompany(Companies company) {
		this.company = company;
	}

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_state", nullable=false)
	public PromotionsState getState() {
		return state;
	}
	public void setState(PromotionsState state) {
		this.state = state;
	}

	@Column(name="rejected_description", nullable=false, length=300)
	public String getRejectedDescription() {
		return rejectedDescription;
	}
	public void setRejectedDescription(String rejectedDescription) {
		this.rejectedDescription = rejectedDescription;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="promotion")
	public Set<Sales> getSales() {
		return sales;
	}
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	}
}
