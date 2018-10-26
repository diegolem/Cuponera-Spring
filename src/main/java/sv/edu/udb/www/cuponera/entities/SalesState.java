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
@Table(name="sales_state", catalog="cuponera")
public class SalesState implements java.io.Serializable{
	
	@Positive
	private int id;
	@Pattern(regexp="^([a-z]|[A-Z]|[ñÑ]){1}[a-zA-Z ñÑáéíóú]*$", message="Ingrese una descripción válida")
	@NotBlank(message="La descripción es obligatoria")
	private String state;
	private Set<Sales> sales = new HashSet<Sales>(0);
	
	public SalesState() {}
	public SalesState(int id, String state) {
		this.id = id;
		this.state = state;
	}
	public SalesState(int id, String state, Set<Sales> sales) {
		super();
		this.id = id;
		this.state = state;
		this.sales = sales;
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
	
	@Column(name="state", nullable=false, length=20)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="state")
	public Set<Sales> getSales() {
		return sales;
	}
	public void setSales(Set<Sales> sales) {
		this.sales = sales;
	}
}
