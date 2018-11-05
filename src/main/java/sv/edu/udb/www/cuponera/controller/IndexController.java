package sv.edu.udb.www.cuponera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Controller
public class IndexController {
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	
	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	
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
					return "index";
			}
		}
		return "index";
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
