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

import sv.edu.udb.www.cuponera.service.EmailService;
import sv.edu.udb.www.cuponera.utils.Mail;
import sv.edu.udb.www.cuponera.utils.Password;
import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
import sv.edu.udb.www.cuponera.entities.simple.SimpleUsers;
import sv.edu.udb.www.cuponera.repositories.UserTypesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository userRepository;
	@Autowired
	@Qualifier("UserTypesRepository")
	UserTypesRepository userTypesRepository;
	
	// /////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String retrieveAllStudents() {
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(SimpleUsers.Parse(this.userRepository.allAdmin()));
		
		
		return jsonInString;
		
		} catch(Exception error) {
			return error.getLocalizedMessage();
		}
	}
	
	@PostMapping("/save")
	public @ResponseBody String saveUser(@RequestParam("name") String name, @RequestParam("lastname") String lastname, @RequestParam("email") String email, @RequestParam("type") int type, @RequestParam("dui") String dui, @RequestParam("nit") String nit) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
		
			if( !this.userRepository.existsDui(dui) ) {
				if (!this.userRepository.existsNit(nit)) {
					if (!this.userRepository.existsEmail(email)) {
						Users user = new Users();
						Optional<UserTypes> userType = this.userTypesRepository.findById(type);
						
						user.setId(0);
						user.setName(name);
						user.setLastName(lastname);
						user.setEmail(email);
						user.setUserType(userType.get());
						user.setDui(dui);
						user.setNit(nit);
						user.setConfirmed((byte)1);
						user.setIdConfirmation("");
						String pass = Password.RandomPassword(6);
						
						user.setPassword(pass);
						
						this.userRepository.saveAndFlush(user);
						
						data.put("state", true);
					} else {
						data.put("state", false);
						data.put("error", "Ya existe un E-Mail ha registrar");
					}
					
				} else {
					data.put("state", false);
					data.put("error", "Ya existe el nit ha registrar");
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
	
	@PutMapping("/update/{id}")
	public @ResponseBody String updateUser(@PathVariable("id")int id, @RequestParam("name") String name, @RequestParam("lastname") String lastname, @RequestParam("email") String email, @RequestParam("type") int type, @RequestParam("dui") String dui, @RequestParam("nit") String nit) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<Users> user = this.userRepository.findById(id);
			
			if	(user.isPresent()) {
				if( !this.userRepository.existsDui(dui, id) ) {
					if (!this.userRepository.existsNit(nit, id)) {
						if (!this.userRepository.existsEmail(email, id)) {
							
							Optional<UserTypes> userType = this.userTypesRepository.findById(type);
							
							user.get().setName(name);
							user.get().setLastName(lastname);
							user.get().setEmail(email);
							user.get().setUserType(userType.get());
							user.get().setDui(dui);
							user.get().setNit(nit);
							
							this.userRepository.saveAndFlush(user.get());
							
							data.put("state", true);
						} else {
							data.put("state", false);
							data.put("error", "Ya existe un E-Mail ha registrar");
						}
						
					} else {
						data.put("state", false);
						data.put("error", "Ya existe el nit ha registrar");
					}
				} else {
					data.put("state", false);
					data.put("error", "Ya existe un dui registrado");
				}
			} else {
				data.put("state", false);
				data.put("error", "El usuario no existe");
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
		
		Optional<Users> user = this.userRepository.findById(id);
		
		try {
			if(user.isPresent()) {
				this.userRepository.deleteById(id);
				data.put("state", true);
			}else {
				data.put("state", false);
				data.put("error", "El usuario no existe");
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
	
	// //////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/list")
	public String listUsers(Model model) {
		model.addAttribute("lista",userRepository.findAll());
		return "Prueba";
	}
	
	@GetMapping("/new")
	public String newUsers(Model model) {
		model.addAttribute("user", new Users());
		return "users/nuevo";
	}
	
	@PostMapping("/new")
	public String insertUsers(@Valid @ModelAttribute("user") Users user,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("user",user);
				return "users/nuevo";
			}else {
				userRepository.save(user);
				return "redirect:/users/list";
			}
		}catch(Exception ex) {
			model.addAttribute("user",user);
			return "users/nuevo";			
		}
	}
	
	@PostMapping("/new_client")
	public String insertClient(@ModelAttribute("user") Users user, BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				return "";
			}else {
				// Validar que no exista el correo, DUI o NIT.
				
				String password = RandomStringUtils.random(8, true, true); // Contraseña aleatoria
				String token = UUID.randomUUID().toString(); // ID de confirmación
				UserTypes userTypes = userTypesRepository.findByType("client"); // Se obtiene tipo de usuario
				
				user.setPassword(password);
				user.setIdConfirmation(token);
				user.setUserType(userTypes);
				userRepository.save(user); // Se guarda el usuario
				
				String message = "Bienvenido a la Cuponera S.A de C.V. <br><br>"
						+ "Contraseña: "+password+""
						+ "Antes debes verificar tú cuenta" + token;
				
				Mail mail = new Mail(user.getEmail(), "Verifiación de cuenta", message);
				EmailService mailService = new EmailService();
				mailService.SendSimpleMessage(mail);
				
				return "";
			}
		}catch(Exception ex) {
			model.addAttribute("user", user);
			return "";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editUsers(@PathVariable("id")int id,Model model) {
		Optional<Users> user = userRepository.findById(id);
		if(user.isPresent()) {
			model.addAttribute("user",user);
			return "users/editar";
		}else {
			return "redirect:/users/list";
		}
	}
	
	@PutMapping("/edit")
	public String editUsers(@Valid @ModelAttribute("user") Users user,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("user",user);
				return "users/editar";
			}else{
				userRepository.save(user);
				return "redirect:/users/list";
			}
		}catch(Exception ex) {
			model.addAttribute("user",user);
			return "users/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteUsers(@PathVariable("id")int id) {
		Optional<Users> user = userRepository.findById(id);
		if(user.isPresent()) {
			userRepository.deleteById(id);
			return "redirect:/users/list";
		}else {
			return "redirect:/users/list";
		}
	}
}
