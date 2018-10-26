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
import org.springframework.web.bind.annotation.RequestMethod;

import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;


@Controller
@RequestMapping("/employee")
public class EmployeesController {

	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String index() {
		return "employee/index";
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
