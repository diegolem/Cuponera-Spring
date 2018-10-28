package sv.edu.udb.www.cuponera.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="sales", catalog="cuponera")
public class Sales implements java.io.Serializable{
	
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){3}[0-9]{3}[0-9]{7}$",message="El formato del código del cupón no es válido")
	@NotNull(message="El codigo del cupon es obligatorio")
	private String couponCode;
	private Promotions promotion;
	private Users client;
	private byte verified;
	@PastOrPresent(message="Ingrese una fecha válida")
	@NotNull(message="La fecha de compra es obligatoria")
	private Date date;
	private SalesState state;
	
	public Sales() {}
	public Sales(String couponCode, Promotions promotion, Users client, byte verified, Date date, SalesState state) {
		this.couponCode = couponCode;
		this.promotion = promotion;
		this.client = client;
		this.verified = verified;
		this.date = date;
		this.state = state;
	}
	
	@Id
	@Column(name="coupon_code", unique=true, nullable=false, length=13)
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="promotion_id", nullable=false)
	public Promotions getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotions promotion) {
		this.promotion = promotion;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="client_id", nullable=false)
	public Users getClient() {
		return client;
	}
	public void setClient(Users client) {
		this.client = client;
	}
	
	@Column(name="verified", nullable=false)
	public byte getVerified() {
		return verified;
	}
	public void setVerified(byte verified) {
		this.verified = verified;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="date", nullable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sales_state", nullable=false)
	public SalesState getState() {
		return state;
	}
	public void setState(SalesState state) {
		this.state = state;
	}
	
}
