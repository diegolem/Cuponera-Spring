package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.UserTypes;

@Repository("UserTypesRepository")
public interface UserTypesRepository extends JpaRepository<UserTypes, Integer>{
	public abstract UserTypes findByType(String type);
}
