package sv.edu.udb.www.cuponera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Controller
public class IndexController {
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = usersRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		return "admin/index";
	}
	
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String clientIndex(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = usersRepository.findByEmail(auth.getName());
		model.addAttribute("user",user);
		return "client/index";
	}
	
	@RequestMapping(value = "/companies", method = RequestMethod.GET)
	public String companyIndex(Model model) {
		return "company/index";
	}
	
	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public String employeeIndex(Model model) {
		return "employee/index";
	}
	
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String denied(Model model) {
		return "denied";
	}
}
