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
import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.SimpleEmployees;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;


@Controller
@RequestMapping("/employee")
public class EmployeesController {
	
	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String index() {
		return "employee/index";
	}
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String retrieveAllStudents() {
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(SimpleEmployees.Parse((this.employeesRepository.findAll())));
		
		
		return jsonInString;
		
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@PutMapping("/update/account")
	public @ResponseBody String updateUser(@RequestParam("name") String name, @RequestParam("lastname") String lastname, @RequestParam("email") String email, @RequestParam("passnew") String passnew, @RequestParam("passnew2") String passnew2, @RequestParam("pass") String pass, @RequestParam("pass2") String pass2) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Employees user = this.employeesRepository.findByEmail(auth.getName());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			if ( !this.usersRepository.existsEmailOnAllTable(email) || !this.employeesRepository.existsEmail(email, user.getId())) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				
				if (pass.equals(pass2) && !pass.trim().equals("")) {
					if(passwordEncoder.matches(pass, user.getPassword())) {
						
						if (passnew.equals(passnew2)) {
							
							Optional<Employees> newUser = this.employeesRepository.findById(user.getId());
							
							if (!passnew.trim().equals("")) newUser.get().setPassword(passwordEncoder.encode(passnew));
							
							newUser.get().setName(name);
							newUser.get().setLastName(lastname);
							newUser.get().setEmail(email);
							
							this.employeesRepository.saveAndFlush(newUser.get());
							
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
		Employees user = this.employeesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", "empleado");
		return "employee/configuration";
	}
	
	@GetMapping("/list")
	public String listEmployees(Model model) {
		model.addAttribute("lista",employeesRepository.findAll());
		return "employee/listar";
	}
	
	@GetMapping("/new")
	public String newEmployees(Model model) {
		model.addAttribute("employee", new Employees());
		return "employee/nuevo";
	}
	
	@PostMapping("/new")
	public String insertEmployees(@Valid @ModelAttribute("employee") Employees employee,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("employee",employee);
				return "employee/nuevo";
			}else {
				employeesRepository.save(employee);
				return "redirect:/employee/list";
			}
		}catch(Exception ex) {
			model.addAttribute("employee",employee);
			return "employee/nuevo";			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editEmployees(@PathVariable("id")int id,Model model) {
		Optional<Employees> employee = employeesRepository.findById(id);
		if(employee.isPresent()) {
			model.addAttribute("employee",employee);
			return "employee/editar";
		}else {
			return "redirect:/employee/list";
		}
	}
	
	@PutMapping("/edit")
	public String editEmployees(@Valid @ModelAttribute("employee") Employees employee,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("employee",employee);
				return "employee/editar";
			}else{
				employeesRepository.save(employee);
				return "redirect:/employee/list";
			}
		}catch(Exception ex) {
			model.addAttribute("employee",employee);
			return "employee/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteEmployees(@PathVariable("id")int id) {
		Optional<Employees> employee = employeesRepository.findById(id);
		if(employee.isPresent()) {
			employeesRepository.deleteById(id);
			return "redirect:/employee/list";
		}else {
			return "redirect:/employee/list";
		}
	}
	
}
