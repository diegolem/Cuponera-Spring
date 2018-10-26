package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.CompanyTypes;

@Repository("CompanyTypesRepository")
public interface CompanyTypesRepository extends JpaRepository<CompanyTypes,Integer> {

}
