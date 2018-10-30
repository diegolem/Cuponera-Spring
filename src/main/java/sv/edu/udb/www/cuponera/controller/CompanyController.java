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

import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.CompanyTypesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.utils.Password;


@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@Autowired
	@Qualifier("CompanyTypesRepository")
	CompanyTypesRepository companyTypesRepository;
	
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
		model.addAttribute("types", this.companyTypesRepository.findAll());
		return "admin/business";
	}
	
	@PostMapping("/save")
	public @ResponseBody String saveCompany(@RequestParam("code") String code, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("address") String address, @RequestParam("pct_comission") int pct_comission, @RequestParam("contact_name") String contact_name, @RequestParam("phone") String phone, @RequestParam("email") String email) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
		
			Companies company = new Companies();
			Optional<CompanyTypes> companyType = this.companyTypesRepository.findById(type);
			
			company.setId(code);
			company.setName(name);
			company.setCompanyType(companyType.get());
			company.setAddress(address);
			company.setPctComission(pct_comission);
			company.setContactName(contact_name);
			company.setTelephone(phone);
			company.setEmail(email);
			company.setPassword(Password.RandomPassword(6));
			
			this.companiesRepository.saveAndFlush(company);
			
			data.put("state", true);
			
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
	
	@DeleteMapping("/remove/{id}")
	public @ResponseBody String removeCompany(@PathVariable("id")String id) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		Optional<Companies> company = companiesRepository.findById(id);
		
		try {
			if(company.isPresent()) {
				companiesRepository.deleteById(id);
				data.put("state", true);
			}else {
				data.put("state", false);
				data.put("error", "La empresa no existe");
			}
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
	
	@PutMapping("/update/{id}")
	public @ResponseBody String updateCompany(@PathVariable("id")String id, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("address") String address, @RequestParam("pct_comission") int pct_comission) {
		Optional<Companies> company = companiesRepository.findById(id);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		if(company.isPresent()) {
			Optional<CompanyTypes> companyType = this.companyTypesRepository.findById(type);
			
			//company.get().setId(code);
			company.get().setName(name);
			company.get().setCompanyType(companyType.get());
			company.get().setAddress(address);
			company.get().setPctComission(pct_comission);
			
			this.companiesRepository.saveAndFlush(company.get());
			
			data.put("state", true);
			
		}else {
			data.put("state", false);
			data.put("error", "La empresa no existe");
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@PutMapping("/update/user/{id}")
	public @ResponseBody String updateCompanyUser(@PathVariable("id")String id, @RequestParam("contact_name") String contact_name, @RequestParam("phone") String phone, @RequestParam("email") String email) {
		Optional<Companies> company = companiesRepository.findById(id);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		if(company.isPresent()) {
			
			//company.get().setId(code);
			company.get().setContactName(contact_name);
			company.get().setTelephone(phone);
			company.get().setEmail(email);
			
			this.companiesRepository.saveAndFlush(company.get());
			
			data.put("state", true);
			
		}else {
			data.put("state", false);
			data.put("error", "La empresa no existe");
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////
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
