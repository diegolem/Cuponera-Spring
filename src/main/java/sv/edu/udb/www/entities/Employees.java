package sv.edu.udb.www.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="employee", catalog="cuponera")
public class Employees implements java.io.Serializable{
	
	@Positive
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
	@NotBlank(message="La contraseña es obligatoria")
	private String password;
	private Companies company;
	
	public Employees() {}
	public Employees(int id, String name, String lastName, String email, String password, Companies company) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.company = company;
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

	@Column(name="name", nullable=false,  length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="name", nullable=false,  length=50)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="email", nullable=false,  length=50, unique=true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="password", nullable=false,  length=64)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="id_company", nullable=false)
	public Companies getCompany() {
		return company;
	}
	public void setCompany(Companies company) {
		this.company = company;
	}
}
