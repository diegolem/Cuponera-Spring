
package sv.edu.udb.www.cuponera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.Users;

@Repository("SalesRepository")
public interface SalesRepository extends JpaRepository<Sales,String> {

	public abstract Sales findByCouponCode(String couponCode);
	
	public abstract List<Sales> findByClient(Users user);
	
	@Query("SELECT p FROM Promotions p WHERE p.state.id = 2 AND p.initDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE AND p.limitDate >= p.endDate ORDER BY p.limitDate")
	public List<Promotions> listPromotionsAvailable();
	
	@Query("select count(s) > 0 from Sales s where s.couponCode = :couponCode")
	public boolean existCupon(@Param("couponCode") String couponCode);
	
	@Query("select s from Sales s where s.client.id = :idUser and s.state.id = 2 and s.promotion.company.id = :company")
	public abstract List<Sales> findByIdClient(@Param("idUser") int idUser, @Param("company") String company);
	
	@Query("select s from Sales s where s.client.id = :idUser and s.state.id != 2 and s.promotion.company.id = :company")
	public abstract List<Sales> findByIdClientHistory(@Param("idUser") int idUser, @Param("company") String company);
}
