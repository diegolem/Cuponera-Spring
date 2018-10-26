package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.PromotionsState;

@Repository("PromotionStateRepository")
public interface PromotionStateRepository extends JpaRepository<PromotionsState,Integer> {

}
