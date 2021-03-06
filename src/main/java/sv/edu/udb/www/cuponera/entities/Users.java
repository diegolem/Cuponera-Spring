package sv.edu.udb.www.cuponera.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="user", catalog="cuponera")
public class Users implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	@Pattern(regexp="^[A-Za-zÑñáéíóú]{1}[A-Za-zÑñáéíóú ]*$",message="Ingrese un nombre correcto")
	@NotBlank(message="El nombre del usuario es obligatorio")
	private String name;
	@Pattern(regexp="^[A-Za-zÑñáéíóú]{1}[A-Za-zÑñáéíóú ]*$",message="Ingrese un apellido correcto")
	@NotBlank(message="El apellido del usuario es obligatorio")
	private String lastName;
	@Email(message="Ingrese un correo correcto")
	@NotBlank(message="El correo es obligatorio")
	private String email;
	// @NotBlank(message="La contraseña es obligatoria")
	private String password;
	private UserTypes userType;
	@NotBlank(message="El DUI es obligatorio")
	@Pattern(regexp="^[0-9]{1}[0-9]{7}[-]{1}[0-9]{1}$")
	private String dui;
	@NotBlank(message="El NIT es obligatorio")
	@Pattern(regexp="^[0-9]{1}[0-9]{3}[-]{1}[0-9]{6}[-]{1}[0-9]{3}[-]{1}[0-9]{1}$")
	private String nit;
	private byte confirmed;
	private String idConfirmation;
	private Set<Sales> sales = new HashSet<Sales>(0);
	
	public Users() {}
	public Users(int id, String name, String lastName, String email, String password, UserTypes userType, String dui,
			String nit, byte confirmed, String idConfirmation) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.dui = dui;
		this.nit = nit;
		this.confirmed = confirmed;
		this.idConfirmation = idConfirmation;
	}
	
	public Users(int id, String name, String lastName, String email, String password, UserTypes userType, String dui,
			String nit, byte confirmed, String idConfirmation, Set<Sales> sales) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.dui = dui;
		this.nit = nit;
		this.confirmed = confirmed;
		this.idConfirmation = idConfirmation;
		this.sales = sales;
	}
	
	public Users(Users user) {
		this.id = user.getId();
		this.name = user.getName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.userType = user.getUserType();
		this.dui = user.getDui();
		this.nit = user.getNit();
		this.confirmed = user.getConfirmed();
		this.idConfirmation = user.getIdConfirmation();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="name", nullable=false, length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="last_name", nullable=false, length=50)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name="email", nullable=false, length=50, unique=true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="password", nullable=false, length=64)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_type", nullable=false)
	public UserTypes getUserType() {
		return userType;
	}
	public void setUserType(UserTypes userType) {
		this.userType = userType;
	}
	
	@Column(name="dui", nullable=false, length=10, unique=true)
	public String getDui() {
		return dui;
	}
	public void setDui(String dui) {
		this.dui = dui;
	}
	
	@Column(name="nit", nullable=false, length=17, unique=true)
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	
	@Column(name="confirmed", nullable=false)
	public byte getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(byte confirmed) {
		this.confirmed = confirmed;
	}
	
	@Column(name="id_confirmation", nullable=true, length=70)
	public String getIdConfirmation() {
		return idConfirmation;
	}
	public void setIdConfirmation(String idConfirmation) {
		this.idConfirmation = idConfirmation;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="client")
	public Set<Sales> getSales() {
		return sales;
	}
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	}
}
