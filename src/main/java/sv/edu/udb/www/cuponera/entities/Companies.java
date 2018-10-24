package sv.edu.udb.www.cuponera.entities;

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

@Entity
@Table(name="company", catalog="cuponera")
public class Companies implements java.io.Serializable{
	
	private String id;
	private String name;
	private String address;
	private String contactName;
	private String telephone;
	private String email;
	private CompanyTypes companyType;
	private int pctComission;
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
