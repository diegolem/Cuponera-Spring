package sv.edu.udb.www.cuponera.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.service.EmailService;
import sv.edu.udb.www.cuponera.utils.Mail;
import sv.edu.udb.www.cuponera.utils.Password;
import sv.edu.udb.www.cuponera.entities.AllUsers;
import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanies;
import sv.edu.udb.www.cuponera.entities.simple.SimpleUsers;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
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
	@Autowired
	EmailService mailService = new EmailService();
	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	// /////////////////////////////////////////////////////////////////////////////////////
	@PostMapping(value = "/exist_mail", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String existMail(@RequestParam("mail") String mail) {
		try {
			
			Map<String, Object> data = new HashMap<>();
			
			if (this.userRepository.existsEmailOnAllTable(mail)) {
				
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String pass = RandomStringUtils.random(8, true, true); // Contraseña aleatoria
				String passnew = passwordEncoder.encode(pass);							
				
				AllUsers user = this.userRepository.findAllUser(mail);
				
				switch (user.getUserType()) 
				{
					case "company":
						Companies companies = this.companiesRepository.findByEmail(mail);
						companies.setPassword(passnew);
						this.companiesRepository.saveAndFlush(companies);
						break;
						
					case "employee":
						Employees employee = this.employeesRepository.findByEmail(mail);
						employee.setPassword(passnew);
						this.employeesRepository.saveAndFlush(employee);
						break;
					
					default:
						Users confUser = this.userRepository.findByEmail(mail);
						confUser.setPassword(passnew);
						this.userRepository.saveAndFlush(confUser);
						break;
				}
				
				data.put("state", true);
				
				String message = "La Cuponera S.A de C.V. le da la bievenida por ese motivo le damos la notificamos que su nueva clave es <br><br>"
						+ "Contraseña: "+ pass +" <br>"
						+ "Puede iniciar session en <a target=\"_blank\" rel=\"noopener noreferrer\" href=\"localhost:8080/login\">Iniciar Sesion</a>";
				
				mailService.SendSimpleMessage(user.getEmail(), "Bienvenido a la cuponera", message);
				
			} else {
				data.put("state", false);
				data.put("error", "No existe el correo");
			}
			
			ObjectMapper mapper = new ObjectMapper();
		
			String jsonInString = mapper.writeValueAsString(data);
		
		
			return jsonInString;
		
		} catch(Exception error) {
			return error.getLocalizedMessage();
		}
	}
	
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
					if (!this.userRepository.existsEmailOnAllTable(email)) {
						Users user = new Users();
						Optional<UserTypes> userType = this.userTypesRepository.findById(type);
						
						BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
						String password = RandomStringUtils.random(8, true, true); // Contraseña aleatoria
						
						user.setId(0);
						user.setName(name);
						user.setLastName(lastname);
						user.setEmail(email);
						user.setUserType(userType.get());
						user.setDui(dui);
						user.setNit(nit);
						user.setConfirmed((byte)1);
						user.setIdConfirmation("");
						
						user.setPassword(passwordEncoder.encode(password));
						
						this.userRepository.saveAndFlush(user);
						
						data.put("state", true);
						
						String message = "Bienvenido a la Cuponera S.A de C.V. <br><br>"
								+ "Contraseña: "+password+" <br>"
								+ "¡En hora buena nuevo administrador! acaba de ser registrado, debe <a href='localhost:8080/login'>Iniciar Sesion</a>";
						
						mailService.SendSimpleMessage(user.getEmail(), "Bienvenido a la cuponera", message);
						
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

	@PutMapping("/update/account")
	public @ResponseBody String updateUser(@RequestParam("name") String name, @RequestParam("lastname") String lastname, @RequestParam("email") String email, @RequestParam("dui") String dui, @RequestParam("nit") String nit, @RequestParam("passnew") String passnew, @RequestParam("passnew2") String passnew2, @RequestParam("pass") String pass, @RequestParam("pass2") String pass2) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = this.userRepository.findByEmail(auth.getName());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			if( !this.userRepository.existsDui(dui, user.getId()) ) {
				if (!this.userRepository.existsNit(nit, user.getId())) {
					if ( !this.userRepository.existsEmailOnAllTable(email) || !this.userRepository.existsEmail(email, user.getId())) {
						BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
						
						if (pass.equals(pass2) && !pass.trim().equals("")) {
							if(passwordEncoder.matches(pass, user.getPassword())) {
								
								if (passnew.equals(passnew2)) {
									
									Optional<Users> newUser = this.userRepository.findById(user.getId());
									
									if (!passnew.trim().equals("")) newUser.get().setPassword(passwordEncoder.encode(passnew));
									
									newUser.get().setName(name);
									newUser.get().setLastName(lastname);
									newUser.get().setEmail(email);
									newUser.get().setDui(dui);
									newUser.get().setNit(nit);
									
									this.userRepository.saveAndFlush(newUser.get());
									
									user.setName(name);
									user.setLastName(lastname);
									user.setEmail(email);
									user.setDui(dui);
									user.setNit(nit);
									user.setPassword(newUser.get().getPassword());
									
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
	
	@GetMapping("/confirmaccount/{token}")
	public String confirmClient(@PathVariable("token")String token, Model model) {
		try {
			Users user = userRepository.findByIdConfirmation(token);
			if(user != null) {
				if(user.getConfirmed() == 0) { //Confirmar
					byte confirmed = 1;
					user.setConfirmed(confirmed);
					userRepository.save(user);
					
					model.addAttribute("confirmMsg", "Usuario verificado");
					return "login";
				}
			}
		}catch(Exception ex) {
			model.addAttribute("errorMsg", "Ha ocurrido un error");
			return "login";
		}
		model.addAttribute("errorMsg", "Ha ocurrido un error");
		return "login";
	}
	
	@PostMapping("/new_client")
	public String insertClient(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model, RedirectAttributes attributes) {
		try {
			if(result.hasErrors()) {
				return "register";
			}else {
				// Validar que no exista el correo, DUI o NIT.
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String password = RandomStringUtils.random(8, true, true); // Contraseña aleatoria
				String token = UUID.randomUUID().toString(); // ID de confirmación
				UserTypes userTypes = userTypesRepository.findByType("client"); // Se obtiene tipo de usuario
				
				user.setPassword(passwordEncoder.encode(password));
				user.setIdConfirmation(token);
				user.setUserType(userTypes);
				byte confirmed = 0;
				user.setConfirmed(confirmed);
				userRepository.saveAndFlush(user); // Se guarda el usuario

				
				String message = "Bienvenido a la Cuponera S.A de C.V. <br><br>"
						+ "Contraseña: "+password+" <br>"
						+ "Antes debes verificar tú cuenta. Copia este link localhost:8080/users/confirmaccount/"+ token;
				
				mailService.SendSimpleMessage(user.getEmail(), "Verificación de cuenta", message);
				
				attributes.addFlashAttribute("confirmMsg", "Favor confirmar cuenta");
				return "redirect:/login";
			}
		}catch(Exception ex) {
			attributes.addFlashAttribute("errorMsg", "Ha ocurrido un error en el proceso");
			model.addAttribute("user", user);
			return "redirect:/register";
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
