package sv.edu.udb.www.cuponera.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanyTypes;
import sv.edu.udb.www.cuponera.entities.simple.SimpleUsers;
import sv.edu.udb.www.cuponera.repositories.CompanyTypesRepository;
import sv.edu.udb.www.cuponera.repositories.UserTypesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.utils.Password;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository userRepository;
	@Autowired
	@Qualifier("CompanyTypesRepository")
	CompanyTypesRepository companyTypesRepository;
	@Autowired
	@Qualifier("UserTypesRepository")
	UserTypesRepository userTypesRepository;
	
	@RequestMapping(value = {"/promotions/"}, method = RequestMethod.GET)
	public String indexPromotions(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("types", this.companyTypesRepository.findAll());
		
		return "admin/promotions";
	}
	
	@RequestMapping(value = {"/companies/"}, method = RequestMethod.GET)
	public String indexCompany(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("types", this.companyTypesRepository.findAll());
		return "admin/business";
	}
	
	@RequestMapping(value = {"/company_type/"}, method = RequestMethod.GET)
	public String indexCompanyType(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		
		return "admin/categories";
	}
	
	@RequestMapping(value = {"/users/"}, method = RequestMethod.GET)
	public String indexUsers(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("types", this.userTypesRepository.allWithoutClient());
		return "admin/users";
	}
}
