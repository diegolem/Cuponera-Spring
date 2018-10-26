package sv.edu.udb.www.cuponera.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import sv.edu.udb.www.cuponera.entities.UserTypes;
import sv.edu.udb.www.cuponera.repositories.UserTypeRepository;

@Controller
@RequestMapping("/user_type")
public class UserTypeController {
	
	@Autowired
	@Qualifier("UserTypeRepository")
	UserTypeRepository userTypeRepository;
	
	@GetMapping("/list")
	public String listTypeUsers(Model model) {
		model.addAttribute("lista",userTypeRepository.findAll());
		return "userType/listar";
	}
	
	@GetMapping("/new")
	public String newUsers(Model model) {
		model.addAttribute("user", new UserTypes());
		return "userType/nuevo";
	}
	
	@PostMapping("/new")
	public String insertTypeUsers(@Valid @ModelAttribute("userType") UserTypes user_type,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("userType",user_type);
				return "userType/nuevo";
			}else {
				userTypeRepository.save(user_type);
				return "redirect:/user_type/list";
			}
		}catch(Exception ex) {
			model.addAttribute("userType",user_type);
			return "userType/nuevo";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editTypeUsers(@PathVariable("id")int id,Model model) {
		Optional<UserTypes> user_type = userTypeRepository.findById(id);
		if(user_type.isPresent()) {
			model.addAttribute("userType",user_type);
			return "userType/editar";
		}else {
			return "redirect:/user_type/list";
		}
	}
	
	@PutMapping("/edit")
	public String editTypeUsers(@Valid @ModelAttribute("userType") UserTypes user_type,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("userType",user_type);
				return "userType/editar";
			}else{
				userTypeRepository.save(user_type);
				return "redirect:/user_type/list";
			}
		}catch(Exception ex) {
			model.addAttribute("userType",user_type);
			return "userType/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteTypeUsers(@PathVariable("id")int id) {
		Optional<UserTypes> user_type = userTypeRepository.findById(id);
		if(user_type.isPresent()) {
			userTypeRepository.deleteById(id);
			return "redirect:/user_type/list";
		}else {
			return "redirect:/user_type/list";
		}
	}
	
}
