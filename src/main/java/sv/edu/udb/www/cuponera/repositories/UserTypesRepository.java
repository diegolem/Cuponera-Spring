package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.UserTypes;

@Repository("UserTypesRepository")
public interface UserTypesRepository extends JpaRepository<UserTypes, Integer>{
	public abstract UserTypes findByType(String type);
	
	@Query("select ut from UserTypes ut where ut.id != 1")
	public abstract List<UserTypes> allWithoutClient();
}
