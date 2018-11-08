package sv.edu.udb.www.cuponera.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
//import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.CompanyTypesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.service.CompanyDetailsImpl;
import sv.edu.udb.www.cuponera.service.EmailService;
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
	
	@Autowired
	EmailService mailService = new EmailService();
	
	@PutMapping("/update/account")
	public @ResponseBody String updateUser(@RequestParam("name") String name, @RequestParam("contactname") String contactname, @RequestParam("address") String address, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("type") int type, @RequestParam("passnew") String passnew, @RequestParam("passnew2") String passnew2, @RequestParam("pass") String pass, @RequestParam("pass2") String pass2) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies user = this.companiesRepository.findByEmail(auth.getName());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			if( !this.companiesRepository.existsName(name, user.getId()) ) {
				
				if ( !this.usersRepository.existsEmailOnAllTable(email) || !this.companiesRepository.existsEmail(email, user.getId())) {
					BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
					
					if (pass.equals(pass2) && !pass.trim().equals("")) {
						if(passwordEncoder.matches(pass, user.getPassword())) {
							
							if (passnew.equals(passnew2)) {
								
								Optional<Companies> newUser = this.companiesRepository.findById(user.getId());
								Optional<CompanyTypes> types = this.companyTypesRepository.findById(type);
								
								if (!passnew.trim().equals("")) newUser.get().setPassword(passwordEncoder.encode(passnew));
								
								newUser.get().setName(name);
								newUser.get().setContactName(contactname);
								newUser.get().setEmail(email);
								newUser.get().setAddress(address);
								newUser.get().setTelephone(phone);
								newUser.get().setCompanyType(types.get());
								
								this.companiesRepository.saveAndFlush(newUser.get());
								
								data.put("state", true);
								
							} else {
								data.put("state", false);
								data.put("error", "Las nuevas claves no coinciden");
							}
							
						} else {
							data.put("state", false);
							data.put("error", "Las clave no es la misma del usuario.");
						}
					} else {
						data.put("state", false);
						data.put("error", "Las claves no coinciden");
					}
				} else {
					data.put("state", false);
					data.put("error", "Ya existe un E-Mail registrado");
				}
					
			} else {
				data.put("state", false);
				data.put("error", "Ya existe un dui registrado");
			}
			
		} catch(Exception error) {
			data.put("state", false);
			data.put("error", error.getLocalizedMessage());
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@RequestMapping(value = {"/configuration/"}, method = RequestMethod.GET)
	public String Configuration(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies user = this.companiesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("types", this.companyTypesRepository.findAll());
		model.addAttribute("userType", "empresa");
		return "company/configuration";
	}
	
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
	
	@PostMapping("/save")
	public @ResponseBody String saveCompany(@RequestParam("code") String code, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("address") String address, @RequestParam("pct_comission") int pct_comission, @RequestParam("contact_name") String contact_name, @RequestParam("phone") String phone, @RequestParam("email") String email) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
		
			if (!this.usersRepository.existsEmailOnAllTable(email)) {
				Companies company = new Companies();
				Optional<CompanyTypes> companyType = this.companyTypesRepository.findById(type);
				
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String password = RandomStringUtils.random(8, true, true); // Contraseña aleatoria
				
				company.setId(code);
				company.setName(name);
				company.setCompanyType(companyType.get());
				company.setAddress(address);
				company.setPctComission(pct_comission);
				company.setContactName(contact_name);
				company.setTelephone(phone);
				company.setEmail(email);
				company.setPassword(passwordEncoder.encode(password));
				
				this.companiesRepository.saveAndFlush(company);
				
				data.put("state", true);

				
				String message = "Bienvenido a la Cuponera S.A de C.V. <br><br>"
						+ "Contraseña: "+password+" <br>"
						+ "Su empresa acaba de ser registrada, debe <a href='localhost:8080/login'>Iniciar Sesion</a>";
				
				mailService.SendSimpleMessage(company.getEmail(), "Bienvenido a la cuponera", message);
			} else {
				data.put("state", false);
				data.put("error", "El correo ya existe");
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
