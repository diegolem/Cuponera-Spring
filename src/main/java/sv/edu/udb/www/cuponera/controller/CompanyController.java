package sv.edu.udb.www.cuponera.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;


@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String retrieveAllStudents() {
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(SimpleCompanies.Parse(this.companiesRepository.findAll()));
		
		
		return jsonInString;
		
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.usersRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		return "admin/business";
	}
	
	@GetMapping("/list")
	public String listCompany(Model model) {
		model.addAttribute("lista",companiesRepository.findAll());
		return "company/listar";
	}
	
	@GetMapping("/new")
	public String newCompany(Model model) {
		model.addAttribute("company", new Companies());
		return "company/nuevo";
	}
	
	@PostMapping("/new")
	public String insertCompany(@Valid @ModelAttribute("company") Companies company,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("company",company);
				return "company/nuevo";
			}else {
				companiesRepository.save(company);
				return "redirect:/company/list";
			}
		}catch(Exception ex) {
			model.addAttribute("company",company);
			return "company/nuevo";	
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editCompany(@PathVariable("id")String id,Model model) {
		Optional<Companies> company = companiesRepository.findById(id);
		if(company.isPresent()) {
			model.addAttribute("company",company);
			return "company/editar";
		}else {
			return "redirect:/company/list";
		}
	}
	
	@PutMapping("/edit")
	public String editCompany(@Valid @ModelAttribute("company") Companies company,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("company",company);
				return "company/editar";
			}else{
				companiesRepository.save(company);
				return "redirect:/company/list";
			}
		}catch(Exception ex) {
			model.addAttribute("company",company);
			return "company/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteCompany(@PathVariable("id")String id) {
		Optional<Companies> company = companiesRepository.findById(id);
		if(company.isPresent()) {
			companiesRepository.deleteById(id);
			return "redirect:/company/list";
		}else {
			return "redirect:/company/list";
		}
	}
	
}