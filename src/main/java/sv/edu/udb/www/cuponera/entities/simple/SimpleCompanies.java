package sv.edu.udb.www.cuponera.entities.simple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.SimpleEmployees;

public class SimpleCompanies {

	private String id;
	private String name;
	private String address;
	private String contactName;
	private String telephone;
	private String email;
	private int idcompanyType;
	private String companyType;
	private int pctComission;
	private String password;
	
	public SimpleCompanies(Companies company) 
	{
		this.id = company.getId();
		this.name = company.getName();
		this.address = company.getAddress();
		this.contactName = company.getContactName();
		this.telephone = company.getTelephone();
		this.email = company.getEmail();
		this.idcompanyType = company.getCompanyType().getId(); 
		this.companyType = company.getCompanyType().getType();
		this.pctComission =  company.getPctComission();
		this.password = company.getPassword();
	}
	
	public static List<SimpleCompanies> Parse(List<Companies> companies)
	{
		List<SimpleCompanies> simples = new ArrayList<>();
		
		for(Companies company : companies) simples.add(new SimpleCompanies(company));
		
		return simples;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public int getPctComission() {
		return pctComission;
	}
	public void setPctComission(int pctComission) {
		this.pctComission = pctComission;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getIdcompanyType() {
		return idcompanyType;
	}

	public void setIdcompanyType(int idcompanyType) {
		this.idcompanyType = idcompanyType;
	}
	
}
