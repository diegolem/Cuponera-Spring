package sv.edu.udb.www.cuponera.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;

@Service
public class SalesService {
	
	@Autowired
	private SalesRepository salesRepository;
	
	private List<Promotions> promotions = new ArrayList<Promotions>();
		
	public Page<Promotions> findPaginated(Pageable pageable){	
		
		promotions = salesRepository.listPromotionsAvailable();
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Promotions> list;
		
		if(promotions.size() < startItem) {
			list = Collections.emptyList();
		}else {
			int toIndex = Math.min(startItem + pageSize, promotions.size());
			list = promotions.subList(startItem, toIndex);
		}
		
		Page<Promotions> promoPage = new PageImpl<Promotions>(list, PageRequest.of(currentPage, pageSize),promotions.size());
		return promoPage;
		
	}
	
}
