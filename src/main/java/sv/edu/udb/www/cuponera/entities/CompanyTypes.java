package sv.edu.udb.www.cuponera.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="company_type", catalog="cuponera")
public class CompanyTypes implements java.io.Serializable{
	
	@Positive
	private int id;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}[a-zA-Z ñÑáéíóú]*$",message="Ingrese un nombre de rubro válido")
	@NotBlank(message="El rubro es obligatorio")
	private String type;
	private Set<Companies> companies = new HashSet<Companies>();
	
	public CompanyTypes() {}
	public CompanyTypes(int id, String type) {
		this.id = id;
		this.type = type;
	}
	public CompanyTypes(int id, String type, Set<Companies> companies) {
		this.id = id;
		this.type = type;
		this.companies = companies;
	}
	
	@Id
	@GeneratedValue(strategy=IDENTITY)
	@Column(name="id", nullable=false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Column(name="type", nullable=false, length=50)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="types")
	public Set<Companies> getCompanies() {
		return companies;
	}
	public void setCompanies(Set<Companies> companies) {
		this.companies = companies;
	}
}
