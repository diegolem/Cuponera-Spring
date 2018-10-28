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
@Table(name="promotion_state", catalog="cuponera")
public class PromotionsState implements java.io.Serializable{
	
	@Positive
	private int id;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}[a-zA-Z ñÑáéíóú]*$", message="Ingrese una descripción válida")
	@NotBlank(message="La descripción es obligatoria")
	private String state;
	private Set<Promotions> promotions = new HashSet<Promotions>(0);
	
	public PromotionsState() {}
	public PromotionsState(int id, String state) {
		this.id = id;
		this.state = state;
	}
	public PromotionsState(int id, String state, Set<Promotions> promotions) {
		super();
		this.id = id;
		this.state = state;
		this.promotions = promotions;
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

	@Column(name="state", nullable=false, length=50)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@OneToMany(fetch=FetchType.EAGER, mappedBy="state")
	public Set<Promotions> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotions> promotions) {
		this.promotions = promotions;
	}
}
