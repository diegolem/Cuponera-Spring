package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.entities.Users;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<Users,Integer> {
	
	 public abstract Users findByEmailAndPasssword(String email,String password);
	 public abstract Users findByEmail(String email);
	
}
