package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Employees;

@Repository("EmployeesRepository")
public interface EmployeesRepository extends JpaRepository<Employees, Integer>{
	public abstract Employees findByEmail(String email);
	public abstract List<Employees> findAll();
}
