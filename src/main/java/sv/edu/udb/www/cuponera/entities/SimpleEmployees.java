package sv.edu.udb.www.cuponera.entities;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class SimpleEmployees {
	
	private int id;
	private String name;
	private String lastName;
	private String email;
	private String password;
	
	private String idCompany;

	public SimpleEmployees(Employees employee) 
	{
		this.id = employee.getId();
		this.name = employee.getName();
		this.lastName = employee.getLastName();
		this.email = employee.getEmail();
		this.password = employee.getPassword();
		this.idCompany = employee.getCompany().getId();
		
	}
	
	public static List<SimpleEmployees> Parse(List<Employees> employees)
	{
		List<SimpleEmployees> simples = new ArrayList<>();
		
		for(Employees employee : employees) simples.add(new SimpleEmployees(employee));
		
		return simples;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(String idCompany) {
		this.idCompany = idCompany;
	}
	
	
}
