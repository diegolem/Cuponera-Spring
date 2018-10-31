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

import sv.edu.udb.www.cuponera.entities.PromotionsState;
import sv.edu.udb.www.cuponera.repositories.PromotionStateRepository;

@Controller
@RequestMapping("/promotion_state")
public class PromotionsStateController {

	@Autowired
	@Qualifier("PromotionStateRepository")
	PromotionStateRepository promotionStateRepository;
	
	@GetMapping("/list")
	public String listPromotionState(Model model) {
		model.addAttribute("lista",promotionStateRepository.findAll());
		return "promotionState/listar";
	}
	
	@GetMapping("/new")
	public String newPromotionState(Model model) {
		model.addAttribute("promotionState", new PromotionsState());
		return "promotionState/nuevo";
	}
	
	@PostMapping("/new")
	public String insertPromotionState(@Valid @ModelAttribute("promotionState") PromotionsState promotion_state,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("promotionState",promotion_state);
				return "promotionState/nuevo";
			}else {
				promotionStateRepository.save(promotion_state);
				return "redirect:/promotion_state/list";
			}
		}catch(Exception ex) {
			model.addAttribute("promotionState",promotion_state);
			return "promotionState/nuevo";	
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editPromotionState(@PathVariable("id")int id,Model model) {
		PromotionsState promotion_state = promotionStateRepository.findById(id);
		if(promotion_state != null) {
			model.addAttribute("promotionState",promotion_state);
			return "promotionState/editar";
		}else {
			return "redirect:/promotion_state/list";
		}
	}
	
	@PutMapping("/edit")
	public String editPromotionState(@Valid @ModelAttribute("promotionState") PromotionsState promotion_state,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("promotionState",promotion_state);
				return "promotionState/editar";
			}else{
				promotionStateRepository.save(promotion_state);
				return "redirect:/promotion_state/list";
			}
		}catch(Exception ex) {
			model.addAttribute("promotionState",promotion_state);
			return "promotionState/editar";			
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deletePromotionState(@PathVariable("id")int id) {
		PromotionsState promotion_state = promotionStateRepository.findById(id);
		if(promotion_state != null) {
			promotionStateRepository.deleteById(id);
			return "redirect:/promotion_state/list";
		}else {
			return "redirect:/promotion_state/list";
		}
	}
}
