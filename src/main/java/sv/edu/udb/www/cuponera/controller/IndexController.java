package sv.edu.udb.www.cuponera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.entities.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
@RequestMapping("/cuponera")
public class IndexController {
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("", "");
		return "login";
	}
	
	@GetMapping("/admin")
	public String adminIndex(Model model) {
		return "admin/index";
	}
}
