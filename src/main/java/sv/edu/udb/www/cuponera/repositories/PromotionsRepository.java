package sv.edu.udb.www.cuponera.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.Promotions;

@Repository("PromotionsRepository")
public interface PromotionsRepository extends JpaRepository<Promotions, Integer> {
	public abstract List<Promotions> findAll();
	public abstract List<Promotions> findByCompany(Companies company);
	public abstract Optional<Promotions> findById(Integer id);
	public abstract Optional<Promotions> findByIdAndCompany(Integer id, Companies company);
}
