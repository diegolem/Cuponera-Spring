package sv.edu.udb.www.cuponera.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
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

import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Controller
@RequestMapping(value= {"client/sales","/sales"})
public class SalesController {

	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository usersRepository;
	
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/index")
	public String indexClientSales() {
		return "client/index";		
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/list")
	public String listSales(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Users user = usersRepository.findByEmail(auth.getName());
		model.addAttribute("lista", salesRepository.findByClient(user));
		return "client/sales/listar";
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/new")
	public String newSales(Model model) {
		model.addAttribute("sales", new Sales());
		model.addAttribute("listPromotion", salesRepository.listPromotionsAvailable());
		return "client/sales/nuevo";
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@PostMapping("/new")
	public String insertSales(@Valid @ModelAttribute("sales") Sales sales,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("sales",sales);
				return "sales/nuevo";
			}else {
				salesRepository.save(sales);
				return "redirect:/client/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "client/sales/nuevo";			
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editSales(@PathVariable("id")String id,Model model) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			model.addAttribute("sales",sales);
			return "sales/editar";
		}else {
			return "redirect:/client/sales/list";
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
				return "redirect:/client/sales/list";
			}
		}catch(Exception ex) {
			model.addAttribute("sales",sales);
			return "sales/editar";			
		}
	}
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@DeleteMapping("/delete/{id}")
	public String deleteSales(@PathVariable("id")String id) {
		Optional<Sales> sales = salesRepository.findById(id);
		if(sales.isPresent()) {
			salesRepository.deleteById(id);
			return "redirect:/client/sales/list";
		}else {
			return "redirect:/client/sales/list";
		}
	}
}
