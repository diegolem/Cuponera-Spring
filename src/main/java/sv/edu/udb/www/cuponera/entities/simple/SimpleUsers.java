package sv.edu.udb.www.cuponera.entities.simple;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.SimpleEmployees;
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;

public class SimpleUsers {
	
	private int id;
	private String name;
	private String lastName;
	private String email;
	private String password;
	private int idType;
	private String type;
	private String dui;
	private String nit;
	private byte confirmed;
	private String idConfirmation;
	
	public SimpleUsers(Users user) 
	{
		this.id = user.getId();
		this.name = user.getName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.idType = user.getUserType().getId();
		this.type = user.getUserType().getType();
		this.dui = user.getDui();
		this.nit = user.getNit();
		this.confirmed = user.getConfirmed();
		this.idConfirmation = (user.getIdConfirmation() == null) ? "" : user.getIdConfirmation();
	}
	
	public static List<SimpleUsers> Parse(List<Users> users)
	{
		List<SimpleUsers> simples = new ArrayList<>();
		
		for(Users user : users) simples.add(new SimpleUsers(user));
		
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

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDui() {
		return dui;
	}

	public void setDui(String dui) {
		this.dui = dui;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public byte getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(byte confirmed) {
		this.confirmed = confirmed;
	}

	public String getIdConfirmation() {
		return idConfirmation;
	}

	public void setIdConfirmation(String idConfirmation) {
		this.idConfirmation = idConfirmation;
	}
}
