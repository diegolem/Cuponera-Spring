package sv.edu.udb.www.cuponera.repositories;

import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository("UsersRepository")
public interface UsersRepository extends JpaRepository<Users, Integer>{
	public abstract Users findByEmailAndPassword(String email, String password);
	public abstract Users findByEmail(String email);
	
	@Query("select count(u) > 0 from Users u where u.dui = :dui")
	public boolean existsDui(@Param("dui") String dui);
	
	@Query("select count(u) > 0 from Users u where u.nit = :nit")
	public boolean existsNit(@Param("nit") String nit);

	@Query("select count(u) > 0 from Users u where u.email = :email")
	public boolean existsEmail(@Param("email") String email);
	
	@Query("select count(u) > 0 from Users u where u.dui = :dui and u.id != :id")
	public boolean existsDui(@Param("dui") String dui, @Param("id") int id);
	
	@Query("select count(u) > 0 from Users u where u.nit = :nit and u.id != :id")
	public boolean existsNit(@Param("nit") String nit, @Param("id") int id);

	@Query("select count(u) > 0 from Users u where u.email = :email and u.id != :id")
	public boolean existsEmail(@Param("email") String email, @Param("id") int id);
}
