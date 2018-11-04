package sv.edu.udb.www.cuponera.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.PromotionsState;

@Repository("PromotionStateRepository")
public interface PromotionStateRepository extends JpaRepository<PromotionsState,Integer> {
	public abstract PromotionsState findById(int id);
	public abstract Optional<PromotionsState> findByState(String state);
}
