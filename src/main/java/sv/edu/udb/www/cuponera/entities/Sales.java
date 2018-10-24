package sv.edu.udb.www.cuponera.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="company_type", catalog="cuponera")
public class Sales implements java.io.Serializable{
	
	private String couponCode;
	private Promotions promotion;
	private Users client;
	private byte verified;
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
	
	@Column(name="promotion_id", nullable=false)
	public Promotions getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotions promotion) {
		this.promotion = promotion;
	}
	
	@Column(name="cleint_id", nullable=false)
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
	
	@Column(name="date", nullable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name="sales_state", nullable=false)
	public SalesState getState() {
		return state;
	}
	public void setState(SalesState state) {
		this.state = state;
	}
}
