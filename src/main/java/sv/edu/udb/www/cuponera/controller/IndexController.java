package sv.edu.udb.www.cuponera.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
import sv.edu.udb.www.cuponera.repositories.PromotionsRepository;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.service.SalesService;

@Controller
public class IndexController {
	private static int currentPage = 1;
	private static int pageSize = 9;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	
	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	
	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	
	@Autowired
	SalesService salesService;	
	
	@Autowired
	@Qualifier("PromotionsRepository")
	PromotionsRepository promotionRepository;
	
	@RequestMapping(value = "/recover_password", method = RequestMethod.GET)
	public String RecoverPassword(Model model) {
		return "/recoverPassword";
	}
	
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String index() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			String userType = auth.getAuthorities().toArray()[0].toString();
			switch(userType) {
				case "ADMINISTRATOR":
					return "redirect:/admin";
				case "COMPANY":
					return "redirect:/companies";
				case "CLIENT":
					return "redirect:/client";
				case "EMPLOYEE":
					return "redirect:/employees";
				default:
					return "redirect:/coupons";
			}
		}
		
		return "redirect:/coupons";
	}
	
	
	@GetMapping("/coupons")
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
		return "sales";
	}
	
	@GetMapping("/coupon/{id}")
	public String details(@PathVariable("id")int id,Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<Promotions> promo = promotionRepository.findById(id);
		
		boolean state = false;
		
		if (promo.isPresent()) 
		{
			model.addAttribute("promotion",promo.get());
			state = promo.get().getState().getId() == 2 && promo.get().getCouponsAvailable() > 0;
			model.addAttribute("max", ( promo.get().getLimitCant() <= promo.get().getCouponsAvailable() )? promo.get().getLimitCant() : promo.get().getCouponsAvailable() );
		}
		
		model.addAttribute("state",state);
		
		return "details";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			String userType = auth.getAuthorities().toArray()[0].toString();
			switch(userType) {
				case "ADMINISTRATOR":
					return "redirect:/admin";
				case "COMPANY":
					return "redirect:/companies";
				case "CLIENT":
					return "redirect:/client";
				case "EMPLOYEE":
					return "redirect:/employees";
				default:
					return "login";
			}
		}
		return "login";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			String userType = auth.getAuthorities().toArray()[0].toString();
			switch(userType) {
				case "ADMINISTRATOR":
					return "redirect:/admin";
				case "COMPANY":
					return "redirect:/companies";
				case "CLIENT":
					return "redirect:/client";
				case "EMPLOYEE":
					return "redirect:/employees";
				default:
					model.addAttribute("user", new Users());
					return "register";
			}
		}
		model.addAttribute("user", new Users());
		return "register";
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = usersRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", user.getUserType().getType());
		return "admin/index";
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String clientIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = usersRepository.findByEmail(auth.getName());
		model.addAttribute("user",user);
		model.addAttribute("userType", user.getUserType().getType());
		return "client/index";
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@RequestMapping(value = "/companies", method = RequestMethod.GET)
	public String companyIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies user = companiesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", "empresa");
		return "company/index";
	}
	
	@PreAuthorize("hasAnyAuthority('EMPLOYEE')")
	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public String employeeIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Employees user = employeesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", "empleado");
		return "employee/index";
	}
	
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String denied(Model model) {
		return "denied";
	}
	
	@RequestMapping(value = "/loginsucess", method = RequestMethod.POST)
	public String loginSuccess(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			String userType = auth.getAuthorities().toArray()[0].toString();
			switch(userType) {
				case "ADMINISTRATOR":
					return "redirect:/admin";
				case "COMPANY":
					return "redirect:/companies";
				case "CLIENT":
					return "redirect:/client";
				case "EMPLOYEE":
					return "redirect:/employees";
				default:
					return "redirect:/login";
			}
		}
		return "redirect:/login";
	}
}
