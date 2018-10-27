package sv.edu.udb.www.cuponera.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Entity
@Table(name="user_type", catalog="cuponera")
public class UserTypes implements java.io.Serializable{
	
	@Positive
	private int id;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}[a-zA-Z ñÑáéíóú]*$", message="Ingrese una descripción válida")
	@NotBlank(message="La descripción es obligatoria")
	private String type;
	private Set<Users> users = new HashSet<Users>(0);
	
	public UserTypes() {}
	public UserTypes(int id, String type) {
		this.id = id;
		this.type = type;
	}
	public UserTypes(int id, String type, Set<Users> users) {
		this.id = id;
		this.type = type;
		this.users = users;
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
	
	@Column(name="type", nullable=false, length=20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="userType")
	public Set<Users> getUsers() {
		return users;
	}
	public void setUsers(Set<Users> users) {
		this.users = users;
	}
}
