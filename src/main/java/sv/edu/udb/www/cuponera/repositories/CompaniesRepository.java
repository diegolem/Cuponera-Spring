package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Companies;;

@Repository("CompaniesRepository")
public interface CompaniesRepository extends JpaRepository<Companies, String>{
	public abstract Companies findByEmail(String email);
	public abstract List<Companies> findAll();

	@Query("select count(u) > 0 from Companies u where u.name = :name and u.id != :id")
	public boolean existsName(@Param("name") String name, @Param("id") String id);
	
	@Query("select count(u) > 0 from Companies u where u.email = :email and u.id != :id")
	public boolean existsEmail(@Param("email") String email, @Param("id") String id);
}
