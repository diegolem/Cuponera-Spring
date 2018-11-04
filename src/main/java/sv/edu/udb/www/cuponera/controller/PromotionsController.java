package sv.edu.udb.www.cuponera.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sv.edu.udb.www.cuponera.entities.Companies;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.entities.CompanyTypes;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.PromotionsState;
import sv.edu.udb.www.cuponera.entities.simple.SimpleCompanyTypes;
import sv.edu.udb.www.cuponera.entities.simple.SimplePromotions;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.PromotionStateRepository;
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
	@Autowired
	@Qualifier("PromotionStateRepository")
	PromotionStateRepository promotionStateRepository;
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@Autowired
	@Qualifier("PromotionStateRepository")
	PromotionStateRepository promotionStateRepository;
	
	// /////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String allTypes() {
		try {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(SimplePromotions.Parse(this.promotionRepository.findAll()));
		
		
		return jsonInString;
		
		} catch(Exception error) {
			return error.getLocalizedMessage();
		}
	}
	
	@PutMapping("/approve/{id}")
	public @ResponseBody String update(@PathVariable("id")int id) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<Promotions> promotion = this.promotionRepository.findById(id);
			
			if	(promotion.isPresent()) {
				
				Optional<PromotionsState> promotionState = this.promotionStateRepository.findById(2);
				
				if (promotionState.isPresent()) {
					promotion.get().setState(promotionState.get());
					
					
					this.promotionRepository.saveAndFlush(promotion.get());
					
					data.put("state", true);
					data.put("error", "Se ha aceptado la promocion");
				} else {
					data.put("state", false);
					data.put("error", "El estado no existe");
				}
				
			} else {
				data.put("state", false);
				data.put("error", "La promocion no existe");
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
	
	@PutMapping("/reject/{id}")
	public @ResponseBody String update(@PathVariable("id")int id, @RequestParam("reject") String reject) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<Promotions> promotion = this.promotionRepository.findById(id);
			
			if	(promotion.isPresent()) {
				
				Optional<PromotionsState> promotionState = this.promotionStateRepository.findById(3);
				
				if (promotionState.isPresent()) {
					promotion.get().setState(promotionState.get());
					promotion.get().setRejectedDescription(reject);
					
					this.promotionRepository.saveAndFlush(promotion.get());
					
					data.put("state", true);
					data.put("error", "Se ha aceptado la promocion");
				} else {
					data.put("state", false);
					data.put("error", "El estado no existe");
				}
				
			} else {
				data.put("state", false);
				data.put("error", "La promocion no existe");
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
	// ////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/list_company")
	public String listPromotionToCompany(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		model.addAttribute("lista", promotionRepository.findByCompany(companiesRepository.findByEmail(auth.getName())));
		return "company/promotion/listar";
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@GetMapping("/list_admin")
	public String listPromotionToAdmin(Model model) {
		model.addAttribute("lista", promotionRepository.findAll());
		return "admin/promotion/listar";
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@GetMapping("/show_admin/{id}")
	public String showPromotionToAdmin(@PathVariable("id")Integer id, Model model) {
		Optional<Promotions> promotion = promotionRepository.findById(id);
		
		if(promotion.isPresent()) {
			model.addAttribute("promotion", promotion.get());
			return "admin/promotion/detalles";
		}else {
			return "redirect:/promotion/list_admin";
		}
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@GetMapping("/show_company/{id}")
	public String showPromotionToCompany(@PathVariable("id")Integer id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Promotions> promotion = promotionRepository.findByIdAndCompany(id, companiesRepository.findByEmail(auth.getName()));
		
		if(promotion.isPresent()) {
			model.addAttribute("promotion", promotion.get());
			return "company/promotion/detalles";
		}else {
			return "redirect:/promotion/list_company";
		}
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@GetMapping("/new")
	public String newPromotion(Model model) {
		model.addAttribute("promotion", new Promotions());
		return "company/promotion/nuevo";
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@PostMapping("/new")
	public String insertPromotion(@RequestParam(name = "image_promotion", required = true) MultipartFile image, @Valid @ModelAttribute("promotion") Promotions promotion,
			BindingResult result, Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("promotion",promotion);
				return "/company/promotion/nuevo";
			}else {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				promotion.setCompany(companiesRepository.findByEmail(auth.getName()));
				promotion.setCouponsAvailable(promotion.getLimitCant());
				promotion.setCouponsSold(0);
				promotion.setImage(image.getOriginalFilename());
				promotion.setChargeService(BigDecimal.valueOf(0));
				promotion.setEarnings(BigDecimal.valueOf(0));
				promotion.setRejectedDescription("");
				promotion.setState(promotionStateRepository.findById(1));

				promotionRepository.saveAndFlush(promotion);
				uploadFileService.saveImage(image);

				return "redirect:/promotion/list_company";
			}
		}catch(Exception ex) {
			model.addAttribute("promotion",promotion);
			return "promotion/nuevo";	
		}
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@GetMapping("/edit/{id}")
	public String editPromotion(@PathVariable("id")Integer id,Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies company = companiesRepository.findByEmail(auth.getName());
		Optional<Promotions> promotion = promotionRepository.findByIdAndCompany(id, company);
		
		if(promotion.isPresent()) {
			model.addAttribute("promotion", promotion.get());
			return "/company/promotion/modificar";
		}else {
			return "redirect:/promotion/list_company";
		}
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@PostMapping("/edit")
	public String editPromotion(@RequestParam("image_promotion") MultipartFile image, @Valid @ModelAttribute("promotion") Promotions promotion,
			BindingResult result, Model model) {
		
		try {
			if(result.hasErrors()) {
				System.out.println("Errores");
				model.addAttribute("promotion", promotion);
				return "/company/promotion/modificar";
			}else{
				if(promotion.getId() != null) {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					Companies company = companiesRepository.findByEmail(auth.getName());
					Optional<Promotions> _p = promotionRepository.findByIdAndCompany(promotion.getId(), company);
					
					if((_p.get().getState().getId() == 3) && (_p.isPresent())) { // Rechazada y pertenece a empresa logueada
						promotion.setState(promotionStateRepository.findById(1));
						promotion.setChargeService(BigDecimal.valueOf(0));
						promotion.setEarnings(BigDecimal.valueOf(0));
						promotion.setRejectedDescription("");
						promotion.setCompany(company);
						promotion.setCouponsAvailable(promotion.getLimitCant());
						promotion.setCouponsSold(0);
						
						if(!image.isEmpty()) { //Mantener misma imagen
							uploadFileService.saveImage(image);
							promotion.setImage(image.getOriginalFilename());
						}else {
							promotion.setImage(_p.get().getImage());
						}

						promotionRepository.save(promotion);
						return "redirect:/promotion/list_company";
					}
				}
				return "redirect:/promotion/edit/"+promotion.getId();
			}
		}catch(Exception ex) {
			model.addAttribute("promotion", promotion);
			return "redirect:/promotion/edit/"+promotion.getId();			
		}
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@PutMapping("/rejected/{id}")
	public ResponseEntity<?> rejectedPromotion(@PathVariable("id")Integer id, @RequestParam(required = true) String description) {
		try {
			Optional<Promotions> promotion = promotionRepository.findById(id);
			
			if(promotion.isPresent()) {
				if(promotion.get().getState().getId() == 1) { //En espera de aprobación
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String initDate = format.format(promotion.get().getInitDate()),
							endDate = format.format(promotion.get().getEndDate()),
							limitDate = format.format(promotion.get().getLimitDate());
					
					promotion.get().setInitDate(format.parse(initDate));
					promotion.get().setEndDate(format.parse(endDate));
					promotion.get().setLimitDate(format.parse(limitDate));
					promotion.get().setRejectedDescription(description);
					promotion.get().setState(promotionStateRepository.findById(3)); // Rechazada
					
					promotionRepository.saveAndFlush(promotion.get());			
					return ResponseEntity.ok("Promoción rechazada");
				}
			}
		}catch(Exception ex) {
			return ResponseEntity.status(500).body("Error en el proceso de rechazo");			
		}
		return ResponseEntity.status(500).body("La promoción no cumple con los parametros para rechazar");
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@PutMapping("/approve/{id}")
	public ResponseEntity<?> approvePromotion(@PathVariable("id")Integer id){
		try {
			Optional<Promotions> promotion = promotionRepository.findById(id);
			
			if(promotion.isPresent()) {
				if(promotion.get().getState().getId() == 1) { //En espera de aprobación
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String initDate = format.format(promotion.get().getInitDate()),
							endDate = format.format(promotion.get().getEndDate()),
							limitDate = format.format(promotion.get().getLimitDate());
					
					promotion.get().setInitDate(format.parse(initDate));
					promotion.get().setEndDate(format.parse(endDate));
					promotion.get().setLimitDate(format.parse(limitDate));
					promotion.get().setState(promotionStateRepository.findById(2)); // Aprobada
				
					promotionRepository.saveAndFlush(promotion.get());
					
					return ResponseEntity.ok("Promoción aprobada");
				}
			}
		}catch(Exception ex) {
			return ResponseEntity.status(500).body("Error en el proceso de aprobación");
		}
		return ResponseEntity.status(500).body("La promoción no cumple con los parametros para ser aprobada");
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePromotion(@PathVariable("id")Integer id) {
		try {
			Optional<Promotions> promotion = promotionRepository.findById(id);
			if(promotion.isPresent()) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Companies company = companiesRepository.findByEmail(auth.getName());
				
				if((promotion.get().getState().getId() == 3) && (promotion.get().getCompany().getId().equals(company.getId()))) {
					promotionRepository.delete(promotion.get());
					return ResponseEntity.ok("Promoción eliminada");
				}
			}
		}catch(Exception ex) {
			return ResponseEntity.status(500).body("Error en el proceso de eliminación");
		}
		return ResponseEntity.status(500).body("La promoción no cumple con los parametros para ser eliminada");
	}
}
