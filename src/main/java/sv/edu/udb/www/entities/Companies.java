package sv.edu.udb.www.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="company", catalog="cuponera")
public class Companies implements java.io.Serializable{
	
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}([a-z]|[A-Z]|[ñÑ]){2}([0-9]){3}$",message="El codigo de la empresa no tiene el formato correcto")
	@NotBlank(message="el codigo de la compañia es obligatoria")
	private String id;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]|[0-9]){1}[a-zA-Z0-9 ñÑáéíóú]*$",message="Ingrese un nombre de compañia válido")
	@NotBlank(message="El nombre de la compañia es obligatorio")
	private String name;
	@Pattern(regexp="^([A-Z]|[a-z]|[ñÑ])[a-zA-Z ñÑáéíóú,0-9.#-]*$",message="Ingrese una dirección válida")
	@NotBlank(message="La dirección es obligatoria")
	private String address;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}[a-zA-Z ñÑáéíóú]*$",message="Ingrese un nombre de contacto válido")
	@NotBlank(message="El nombre del contacto es obligatorio")
	private String contactName;
	@Pattern(regexp="^[267]{1}[0-9]{3}([- ])[0-9]{4}$",message="Ingrese un telefono válido")
	@NotBlank(message="El telefono es obligatorio")
	private String telephone;
	@Email(message="Ingrese un correo válido")
	@NotBlank(message="El correo es obligatorio")
	private String email;
	private CompanyTypes companyType;
	@Min(value= 0,message="La comision no puede ser negativa")
	@Max(value= 100,message="La comision no debe ser mayor al 100%")
	@NotBlank(message="El porcentaje de comision es obligatorio")
	private int pctComission;
	@NotBlank(message="La contraseña es obligatoria")
	private String password;
	private Set<Employees> employees = new HashSet<Employees>(0);
	private Set<Promotions> promotions = new HashSet<Promotions>(0);
	
	public Companies() {}
	public Companies(String id, String name, String address, String contact_name, String telephone, String email,
			CompanyTypes companyType, int pctComission, String password) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.contactName = contact_name;
		this.telephone = telephone;
		this.email = email;
		this.companyType = companyType;
		this.pctComission = pctComission;
		this.password = password;
	}
	
	public Companies(String id, String name, String address, String contactName, String telephone, String email,
			CompanyTypes companyType, int pctComission, String password, Set<Employees> employees,
			Set<Promotions> promotions) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.contactName = contactName;
		this.telephone = telephone;
		this.email = email;
		this.companyType = companyType;
		this.pctComission = pctComission;
		this.password = password;
		this.employees = employees;
		this.promotions = promotions;
	}
	
	@Id
	@Column(name="id", unique=true, nullable=false, length=6)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="name", nullable=false, length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="address", nullable=false, length=100)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="contact_name", nullable=false, length=50)
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Column(name="telephone", nullable=false, length=9)
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Column(name="email", nullable=false, length=50, unique=true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="type_company", nullable=false)
	public CompanyTypes getCompanyType() {
		return companyType;
	}
	public void setCompanyType(CompanyTypes companyType) {
		this.companyType = companyType;
	}
	
	@Column(name="pct_comission", nullable=false)
	public int getPctComission() {
		return pctComission;
	}
	public void setPctComission(int pctComission) {
		this.pctComission = pctComission;
	}
	
	@Column(name="password", nullable=false, length=64)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="company")
	public Set<Employees> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Employees> employees) {
		this.employees = employees;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="company")
	public Set<Promotions> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotions> promotions) {
		this.promotions = promotions;
	}
}
