package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Companies;;

@Repository("CompaniesRepository")
public interface CompaniesRepository extends JpaRepository<Companies, String>{
	public abstract Companies findByEmail(String email);
}
