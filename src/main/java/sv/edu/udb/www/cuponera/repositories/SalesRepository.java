package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Sales;

@Repository("SalesRepository")
public interface SalesRepository extends JpaRepository<Sales,String> {

}
