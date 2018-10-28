package sv.edu.udb.www.cuponera.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.PromotionsRepository;
import sv.edu.udb.www.cuponera.service.UploadFileService;

@Controller
@RequestMapping("/promotion")
public class PromotionsController {
	@Autowired
	private UploadFileService uploadFileService;
	
	@Autowired
	@Qualifier("PromotionsRepository")
	PromotionsRepository promotionRepository;
	
	@Autowired
	@Qualifier("CompaniesRepository")
	CompaniesRepository companiesRepository;
	
	@GetMapping("/list_company")
	public String listPromotionToCompany(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		model.addAttribute("lista", promotionRepository.findByCompany(companiesRepository.findByEmail(auth.getName())));
		return "company/promotion/listar";
	}
	
	@GetMapping("/list_admin")
	public String listPromotionToAdmin(Model model) {
		model.addAttribute("lista", promotionRepository.findAll());
		return "admin/promotion/listar";
	}
	
	@GetMapping("/new")
	public String newPromotion(Model model) {
		model.addAttribute("promotion", new Promotions());
		return "company/promotion/nuevo";
	}
	
	@PostMapping("/new")
	public String insertPromotion(@RequestParam("image") MultipartFile image, @Valid @ModelAttribute("promotion") Promotions promotion,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("promotion",promotion);
				return "company/promotion/nuevo";
			}else {
				promotion.setCouponsAvailable(promotion.getLimitCant());
				promotionRepository.save(promotion);
				
				uploadFileService.saveImage(image);
				return "redirect:/promotion/list_company";
			}
		}catch(Exception ex) {
			model.addAttribute("promotion",promotion);
			return "promotion/nuevo";	
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editPromotion(@PathVariable("id")Integer id,Model model) {
		Optional<Promotions> promotion = promotionRepository.findById(id);
		if(promotion.isPresent()) {
			model.addAttribute("promotion",promotion);
			return "promotion/editar";
		}else {
			return "redirect:/promotion/list";
		}
	}
	
	@PutMapping("/edit")
	public String editPromotion(@Valid @ModelAttribute("promotion") Promotions promotion,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("promotion",promotion);
				return "promotion/editar";
			}else{
				promotionRepository.save(promotion);
				return "redirect:/promotion/list";
			}
		}catch(Exception ex) {
			model.addAttribute("promotion",promotion);
			return "promotion/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deletePromotion(@PathVariable("id")Integer id) {
		Optional<Promotions> promotion = promotionRepository.findById(id);
		if(promotion.isPresent()) {
			promotionRepository.deleteById(id);
			return "redirect:/promotion/list";
		}else {
			return "redirect:/promotion/list";
		}
	}
}
