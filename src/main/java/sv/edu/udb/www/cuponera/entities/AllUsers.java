package sv.edu.udb.www.cuponera.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="all_users", catalog="cuponera")
public class AllUsers implements java.io.Serializable{
	@Id
	@Column(name="email", nullable=false, length=50)
	private String email;
	@Column(name="password", nullable=false, length=64)
	private String password;
	@Column(name="user_type", nullable=false, length=20)
	private String userType;
	@Column(name="confirmed", nullable=false, length=4)
	private String confirmed;
	@Column(name="id_confirmation", nullable=true, length=70)
	private String idConfirmation;
	
	public AllUsers() { }
	public AllUsers(String email, String password, String userType, String confirmed, String idConfirmation) {
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.confirmed = confirmed;
		this.idConfirmation = idConfirmation;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}
	public String getIdConfirmation() {
		return idConfirmation;
	}
	public void setIdConfirmation(String idConfirmation) {
		this.idConfirmation = idConfirmation;
	}
}
