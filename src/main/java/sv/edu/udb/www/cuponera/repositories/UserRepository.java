package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<Users,Integer> {
	
	 public abstract Users findByEmailAndPassword(String email,String password);
	 public abstract Users findByEmail(String email);
}
