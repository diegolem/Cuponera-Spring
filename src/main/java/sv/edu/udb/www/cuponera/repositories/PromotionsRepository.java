package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.Promotions;

@Repository("PromotionsRepository")
public interface PromotionsRepository extends JpaRepository<Promotions,Integer> {

}
