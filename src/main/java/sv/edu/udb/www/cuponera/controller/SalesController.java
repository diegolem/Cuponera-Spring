package sv.edu.udb.www.cuponera.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleSales;
import sv.edu.udb.www.cuponera.entities.simple.SimpleUsers;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.service.SalesService;

@Controller
@RequestMapping(value= {"client/sales","/sales"})
public class SalesController {

	private static int currentPage = 1;
	private static int pageSize = 9;
	
	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@Autowired
	SalesService salesService;	
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/index")
	public String indexClientSales() {
		return "client/index";		
	}
	
	@RequestMapping(value = {"/find/{code}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String GetSaleByDui(@PathVariable("code") String code){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			data.put("state", true);
			data.put("sale", new SimpleSales(this.salesRepository.findByCouponCode(code)));
		} catch(Exception error) {
			data.put("state", false);
			data.put("error", error.getMessage());
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/list")
	public String listSales(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Users user = this.usersRepository.findByEmail(auth.getName());
		if ( user != null ) {
			model.addAttribute("user", user);
			model.addAttribute("userType", user.getUserType().getType());
		}
		
		model.addAttribute("lista", salesRepository.findByClient(user));
		return "client/sales/listar";
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/new")
	public String newSales(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		page.ifPresent(p -> currentPage = p);
		size.ifPresent(s -> pageSize = s);
		
		Page<Promotions> promoPage = salesService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
		model.addAttribute("listPromotion", promoPage);

		int totalPages = promoPage.getTotalPages();
		if(totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
			
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Users user = this.usersRepository.findByEmail(auth.getName());
		if ( user != null ) {
			model.addAttribute("user", user);
			model.addAttribute("userType", user.getUserType().getType());
		}
		
		//model.addAttribute("listPromotion", salesRepository.listPromotionsAvailable());
		return "client/sales/nuevo";
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@PostMapping("/new")
	public String insertSales(@Valid @ModelAttribute("sales") Sales sales,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("sales",sales);
				return "sales/nuevo";
			}else {
				salesRepository.save(sales);
				return "redirect:/client/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "client/sales/nuevo";			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editSales(@PathVariable("id")String id,Model model) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			model.addAttribute("sales",sales);
			return "sales/editar";
		}else {
			return "redirect:/client/sales/list";
		}
	}
	
	
	@PutMapping("/edit")
	public String editSales(@Valid @ModelAttribute("sales") Sales sales,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("sales",sales);
				return "sales/editar";
			}else{
				salesRepository.save(sales);
				return "redirect:/client/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "sales/editar";			
		}
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@DeleteMapping("/delete/{id}")
	public String deleteSales(@PathVariable("id")String id) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			salesRepository.deleteById(id);
			return "redirect:/client/sales/list";
		}else {
			return "redirect:/client/sales/list";
		}
	}
}
