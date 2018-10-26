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

import sv.edu.udb.www.cuponera.entities.SalesState;
import sv.edu.udb.www.cuponera.repositories.SalesStateRepository;

@Controller
@RequestMapping("/sales_state")
public class SalesStateController {

	@Autowired
	@Qualifier("SalesStateRepository")
	SalesStateRepository salesStateRepository;
	
	@GetMapping("/list")
	public String listSalesState(Model model) {
		model.addAttribute("lista",salesStateRepository.findAll());
		return "salesState/listar";
	}
	
	@GetMapping("/new")
	public String newSalesState(Model model) {
		model.addAttribute("salesState", new SalesState());
		return "salesState/nuevo";
	}
	
	@PostMapping("/new")
	public String insertSalesState(@Valid @ModelAttribute("salesState") SalesState sales_state,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("salesState",sales_state);
				return "salesState/nuevo";
			}else {
				salesStateRepository.save(sales_state);
				return "redirect:/sales_state/list";
			}
		}catch(Exception ex) {
			model.addAttribute("salesState",sales_state);
			return "salesState/nuevo";			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editSalesState(@PathVariable("id")int id,Model model) {
		Optional<SalesState> sales_state = salesStateRepository.findById(id);
		if(sales_state.isPresent()) {
			model.addAttribute("salesState",sales_state);
			return "salesState/editar";
		}else {
			return "redirect:/sales_state/list";
		}
	}
	
	@PutMapping("/edit")
	public String editSalesState(@Valid @ModelAttribute("salesState") SalesState sales_state,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("salesState",sales_state);
				return "salesState/editar";
			}else{
				salesStateRepository.save(sales_state);
				return "redirect:/sales_state/list";
			}
		}catch(Exception ex) {
			model.addAttribute("salesState",sales_state);
			return "salesState/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteSalesState(@PathVariable("id")int id) {
		Optional<SalesState> sales_state = salesStateRepository.findById(id);
		if(sales_state.isPresent()) {
			salesStateRepository.deleteById(id);
			return "redirect:/sales_state/list";
		}else {
			return "redirect:/sales_state/list";
		}
	}
	
}
