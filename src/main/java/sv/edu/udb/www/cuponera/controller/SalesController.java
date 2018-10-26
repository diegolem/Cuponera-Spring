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

import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;

@Controller
@RequestMapping("/sales")
public class SalesController {

	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	
	@GetMapping("/list")
	public String listSales(Model model) {
		model.addAttribute("lista",salesRepository.findAll());
		return "sales/listar";
	}
	
	@GetMapping("/new")
	public String newSales(Model model) {
		model.addAttribute("sales", new Sales());
		return "sales/nuevo";
	}
	
	@PostMapping("/new")
	public String insertSales(@Valid @ModelAttribute("sales") Sales sales,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("sales",sales);
				return "sales/nuevo";
			}else {
				salesRepository.save(sales);
				return "redirect:/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "sales/nuevo";			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editSales(@PathVariable("id")String id,Model model) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			model.addAttribute("sales",sales);
			return "sales/editar";
		}else {
			return "redirect:/sales/list";
		}
	}
	
	@PutMapping("/edit")
	public String editSales(@Valid @ModelAttribute("sales") Sales sales,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("sales",sales);
				return "sales/editar";
			}else{
				salesRepository.save(sales);
				return "redirect:/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "sales/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteSales(@PathVariable("id")String id) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			salesRepository.deleteById(id);
			return "redirect:/sales/list";
		}else {
			return "redirect:/sales/list";
		}
	}
	
}
