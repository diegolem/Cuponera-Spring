package sv.edu.udb.www.cuponera.controller;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import sv.edu.udb.www.cuponera.service.EmailService;
import sv.edu.udb.www.cuponera.utils.Mail;
import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.entities.Users;
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
					
					model.addAttribute("messageConfirm", "Usuario verificado");
					return "login";
				}
			}
		}catch(Exception ex) {
			model.addAttribute("messageError", "Ha ocurrido un error");
			return "login";
		}
		model.addAttribute("messageError", "Ha ocurrido un error");
		return "login";
	}
	
	@PostMapping("/new_client")
	public String insertClient(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model) {
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
						+ "Antes debes verificar tú cuenta <a href='localhost:8080/users/confirmaccount/"+ token +"'> Click Aquí</a>";
				
				mailService.SendSimpleMessage(user.getEmail(), "Verifiación de cuenta", message);
				
				return "redirect:/login";
			}
		}catch(Exception ex) {
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
