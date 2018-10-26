package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.www.cuponera.entities.AllUsers;

@Repository("AllUsersRepository")
public interface AllUsersRepository extends JpaRepository<AllUsers, String>{
	public abstract AllUsers findByEmail(String email);
}
