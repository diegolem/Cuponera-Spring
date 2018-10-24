package sv.edu.udb.www.cuponera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.entities.Users;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<Users,Integer> {
	
	public abstract Users findByEmailAndPassword(String email,String password);
	public abstract Users findByEmail(String email);
	
}
