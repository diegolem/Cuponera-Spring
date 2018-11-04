package sv.edu.udb.www.cuponera.entities.simple;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.CompanyTypes;

public class SimpleCompanyTypes {
	private int id;
	private String type;
	
	public SimpleCompanyTypes(CompanyTypes type) 
	{
		this.id = type.getId();
		this.type = type.getType();
	}
	
	public static List<SimpleCompanyTypes> Parse(List<CompanyTypes> CompanyTypes)
	{
		List<SimpleCompanyTypes> simples = new ArrayList<>();
		
		for(CompanyTypes type : CompanyTypes) simples.add(new SimpleCompanyTypes(type));
		
		return simples;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
