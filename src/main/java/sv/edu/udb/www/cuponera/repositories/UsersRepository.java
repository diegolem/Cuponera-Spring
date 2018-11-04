package sv.edu.udb.www.cuponera.repositories;

import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository("UsersRepository")
public interface UsersRepository extends JpaRepository<Users, Integer>{
	public abstract Users findByEmailAndPassword(String email, String password);
	public abstract Users findByEmail(String email);
	public abstract Users findByIdConfirmation(String idConfirmation);
}
