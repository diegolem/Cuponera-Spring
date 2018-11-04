package sv.edu.udb.www.cuponera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.edu.udb.www.cuponera.entities.CompanyTypes;

@Repository("CompanyTypesRepository")
public interface CompanyTypesRepository extends JpaRepository<CompanyTypes,Integer> {
	@Query("select count(t) > 0 from CompanyTypes t where t.type = :type")
	public boolean existsType(@Param("type") String type);
	
	@Query("select count(t) > 0 from CompanyTypes t where t.type = :type and t.id != :id")
	public boolean existsType(@Param("type") String type, @Param("id") int id);
}
