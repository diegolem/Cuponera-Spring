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
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.utils.Password;

@Controller
@RequestMapping("/company_type")
public class CompanyTypesController {

	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository userRepository;
	@Autowired
	@Qualifier("CompanyTypesRepository")
	CompanyTypesRepository companyTypesRepository;
	
	// ///////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		
		return "admin/categories";
	}
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String allTypes() {
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(SimpleCompanyTypes.Parse(this.companyTypesRepository.findAll()));
		
		
		return jsonInString;
		
		} catch(Exception error) {
			return error.getLocalizedMessage();
		}
	}
	
	@PostMapping("/save")
	public @ResponseBody String save(@RequestParam("type") String type) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
		
			if( !this.companyTypesRepository.existsType(type) ) {
				CompanyTypes companyType = new CompanyTypes();
				
				companyType.setType(type);
				
				this.companyTypesRepository.saveAndFlush(companyType);
				
				data.put("state", true);
			} else {
				data.put("state", false);
				data.put("error", "Ya existe el rubro");
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
	
	@DeleteMapping("/remove/{id}")
	public @ResponseBody String removeUser(@PathVariable("id")int id) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		Optional<CompanyTypes> companyTypes = this.companyTypesRepository.findById(id);
		
		try {
			if(companyTypes.isPresent()) {
				this.companyTypesRepository.deleteById(id);
				data.put("state", true);
			}else {
				data.put("state", false);
				data.put("error", "El rubro no existe");
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
	public @ResponseBody String update(@PathVariable("id")int id, @RequestParam("type") String type) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<CompanyTypes> types = this.companyTypesRepository.findById(id);
			
			if	(types.isPresent()) {
				if( !this.companyTypesRepository.existsType(type, id) ) {
					
					types.get().setType(type);
					
					this.companyTypesRepository.saveAndFlush(types.get());
					
					data.put("state", true);
				} else {
					data.put("state", false);
					data.put("error", "Ya existe un rubro");
				}
			} else {
				data.put("state", false);
				data.put("error", "El rubro no existe");
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
	
	// ///////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/list")
	public String listCompanyTypes(Model model) {
		model.addAttribute("lista",companyTypesRepository.findAll());
		return "companyType/listar";
	}
	
	@GetMapping("/new")
	public String newCompanyType(Model model) {
		model.addAttribute("companyType", new CompanyTypes());
		return "companyType/nuevo";
	}
	
	@PostMapping("/new")
	public String insertCompanyType(@Valid @ModelAttribute("companyType") CompanyTypes company_type,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("companyType",company_type);
				return "companyType/nuevo";
			}else {
				companyTypesRepository.save(company_type);
				return "redirect:/company_type/list";
			}
		}catch(Exception ex) {
			model.addAttribute("companyType",company_type);
			return "companyType/nuevo";	
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editCompanyType(@PathVariable("id")int id,Model model) {
		Optional<CompanyTypes> company_types = companyTypesRepository.findById(id);
		if(company_types.isPresent()) {
			model.addAttribute("companyType",company_types);
			return "companyType/editar";
		}else {
			return "redirect:/company_type/list";
		}
	}
	
	@PutMapping("/edit")
	public String editCompanyType(@Valid @ModelAttribute("companyType") CompanyTypes company_types,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("companyType",company_types);
				return "companyType/editar";
			}else{
				companyTypesRepository.save(company_types);
				return "redirect:/company_type/list";
			}
		}catch(Exception ex) {
			model.addAttribute("companyType",company_types);
			return "companyType/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteCompanyType(@PathVariable("id")int id) {
		Optional<CompanyTypes> company_types = companyTypesRepository.findById(id);
		if(company_types.isPresent()) {
			companyTypesRepository.deleteById(id);
			return "redirect:/company_type/list";
		}else {
			return "redirect:/company_type/list";
		}
	}
	
}
