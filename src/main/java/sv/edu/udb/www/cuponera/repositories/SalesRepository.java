package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.Users;

@Repository("SalesRepository")
public interface SalesRepository extends JpaRepository<Sales,String> {

	public abstract List<Sales> findByClient(Users user);
}
