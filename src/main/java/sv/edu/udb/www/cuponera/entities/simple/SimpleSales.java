package sv.edu.udb.www.cuponera.entities.simple;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.SalesState;
import sv.edu.udb.www.cuponera.entities.Users;

public class SimpleSales {
	private String couponCode;
	
	private SimplePromotions promotion;
	
	private SimpleUsers client;
	
	private byte verified;
	
	private String date;
	
	private int idState;
	private String state;
	
	public SimpleSales(Sales sale)  {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		
		this.couponCode = sale.getCouponCode();
		this.promotion = new SimplePromotions(sale.getPromotion());
		this.client = new SimpleUsers(sale.getClient());
		this.verified = sale.getVerified();
		if (sale.getDate() != null) this.date = format.format(sale.getDate());
		this.idState = sale.getState().getId();
		this.state = sale.getState().getState();
	}
	
	public static List<SimpleSales> Parse(List<Sales> sales)
	{
		List<SimpleSales> simples = new ArrayList<>();
		
		for(Sales sale : sales) simples.add(new SimpleSales(sale));
		
		return simples;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public SimplePromotions getPromotion() {
		return promotion;
	}

	public void setPromotion(SimplePromotions promotion) {
		this.promotion = promotion;
	}

	public SimpleUsers getClient() {
		return client;
	}

	public void setClient(SimpleUsers client) {
		this.client = client;
	}

	public byte getVerified() {
		return verified;
	}

	public void setVerified(byte verified) {
		this.verified = verified;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIdState() {
		return idState;
	}

	public void setIdState(int idState) {
		this.idState = idState;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
