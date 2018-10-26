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

import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.repositories.CompanyTypesRepository;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;

@Controller
@RequestMapping("/company_type")
public class CompanyTypesController {

	@Autowired
	@Qualifier("CompanyTypesRepository")
	CompanyTypesRepository companyTypesRepository;
	
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
