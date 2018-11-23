package sv.edu.udb.www.cuponera.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sv.edu.udb.www.cuponera.entities.AllUsers;
import sv.edu.udb.www.cuponera.entities.Companies;
import sv.edu.udb.www.cuponera.entities.Employees;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.pdf.codec.Base64.OutputStream;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import sv.edu.udb.www.cuponera.entities.Promotions;
import sv.edu.udb.www.cuponera.entities.PromotionsState;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.SalesState;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimplePromotions;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.PromotionStateRepository;
import sv.edu.udb.www.cuponera.repositories.PromotionsRepository;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;
import sv.edu.udb.www.cuponera.repositories.SalesStateRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;
import sv.edu.udb.www.cuponera.service.EmailService;
import sv.edu.udb.www.cuponera.service.SalesService;
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
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository userRepository;
	@Autowired
	EmailService mailService = new EmailService();
	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	@Autowired
	@Qualifier("SalesStateRepository")
	SalesStateRepository salesStateRepository;
	@Autowired
	SalesService salesService;	
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@GetMapping("/bill")
	 public void export(ModelAndView model, HttpServletResponse response, @RequestParam("code") List<String> codes) throws Exception{
	  JasperPrint jasperPrint = null;

	  response.setContentType("application/x-download");
	  response.setHeader("Content-Disposition", String.format("attachment; filename=\"factura.pdf\""));
	  
	  ServletOutputStream out = response.getOutputStream();
	  jasperPrint = this.salesService.exportPdfFile(codes);
	  JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	 }
	
	@PreAuthorize("hasAnyAuthority('CLIENT')")
	@PostMapping("/buy")
	public @ResponseBody String buy(@RequestParam("id") int id, @RequestParam("cant") int cant) 
	{
		try {
			Map<String, Object> data = new HashMap<>();
			
			if (this.promotionRepository.existsById(id)) {
										
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Users user = this.userRepository.findByEmail(auth.getName());
				
				if (user != null) 
				{
					
					Optional<Promotions> promo = promotionRepository.findById(id);
					
					if (promo.get().getLimitCant() == 0 || promo.get().getLimitCant() >= cant) {
						if (promo.get().getLimitCant() == 0 || promo.get().getCouponsAvailable() >= cant) {
							
							//promo.get().setCouponsAvailable(promo.get().getCouponsAvailable() - cant);
							//promo.get().setCouponsSold(promo.get().getCouponsSold() + cant);
							
							List<String> ids = new ArrayList();
							List<String> codes = new ArrayList();
							
							Optional<SalesState> stat = this.salesStateRepository.findById(2);
							
							for (int i = 0; i < cant; i++) {
			                    Sales salesN = new Sales();

			                    //Asignando el codigo del cupon
			                    salesN.setCouponCode(this.generateCode(promo.get().getCompany()));
			                    //Asignando el codigo de la promotion
			                    salesN.setPromotion(promo.get());
			                    //Asignando el Cliente
			                    salesN.setClient(user);
			                    //Falta asignar el estado
			                    salesN.setState(stat.get());
			                    //Realizando el proceso 
			                    if (this.salesService.insertSales(salesN)) {
			                    //Añadiendo el codigo del cupon a un array 
			                    	codes.add(salesN.getCouponCode());
			                        ids.add("Codigo del cupon: " + salesN.getCouponCode() + ", promocion: " + promo.get().getTitle());
			                    }
			                }
							
							if (ids.size() == cant) {
								data.put("state", true);
								data.put("codes", codes);
								
								String message = "La Cuponera S.A de C.V. Le avisa sobre una compra realizada recientemente <br><br>"
										+ "Total de cupones: "+ cant +" <br>";
								
								for(String msg : ids) 
								{
									message += msg + "<br>";
								}
								
								
								mailService.SendSimpleMessage(user.getEmail(), "Compra realizada", message);
			                } else {
			                	data.put("state", false);
								data.put("error", "No se ha podido comprar los cupones");
			                }
							
							
						} else {
							data.put("state", false);
							data.put("error", "No hay suficiente existencia para realizar la compra");
						}
					} else {
						data.put("state", false);
						data.put("error", "La cantidad sobrepasa el limite de compra");
					}
					
				} else {
					data.put("state", false);
					data.put("error", "Usuario no identificado");
				}
				
			} else {
				data.put("state", false);
				data.put("error", "La promocion no existe");
			}
			
			ObjectMapper mapper = new ObjectMapper();
		
			String jsonInString = mapper.writeValueAsString(data);
		
		
			return jsonInString;
		
		} catch(Exception error) {
			return "Error: " + error.getMessage();
		}
	}
	
	
	@GetMapping("/details/{id}")
	public String details(@PathVariable("id")int id,Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<Promotions> promo = promotionRepository.findById(id);
		
		boolean state = false;
		
		if (promo.isPresent()) 
		{
			model.addAttribute("promotion",promo.get());
			state = promo.get().getState().getId() == 2 && (promo.get().getCouponsAvailable() > 0 || promo.get().getLimitCant() == 0);
			if (promo.get().getLimitCant() > 0)
				model.addAttribute("max", ( promo.get().getLimitCant() <= promo.get().getCouponsAvailable() )? promo.get().getLimitCant() : promo.get().getCouponsAvailable() );
			else
				model.addAttribute("max", 100);
		}
		
		model.addAttribute("state",state);
		
		Users user = this.userRepository.findByEmail(auth.getName());
		if ( user != null ) {
			model.addAttribute("user", user);
			model.addAttribute("userType", user.getUserType().getType());
		}
		
		return "client/sales/details";
	}
	
	
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
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@PutMapping("/approved/{id}")
	public @ResponseBody String update(@PathVariable("id")int id) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<Promotions> promotion = this.promotionRepository.findById(id);
			
			if	(promotion.isPresent()) {
				
				Optional<PromotionsState> promotionState = this.promotionStateRepository.findByState("Aprobada");
				
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
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
	@PutMapping("/reject/{id}")
	public @ResponseBody String update(@PathVariable("id")int id, @RequestParam("reject") String reject) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Optional<Promotions> promotion = this.promotionRepository.findById(id);
			
			if	(promotion.isPresent()) {
				
				Optional<PromotionsState> promotionState = this.promotionStateRepository.findByState("Rechazada");
				
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
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@GetMapping("/list_company")
	public String listPromotionToCompany(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies user = companiesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", "empresa");
		
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
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Companies user = companiesRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("userType", "empresa");
		
		model.addAttribute("promotion", new Promotions());
		return "company/promotion/nuevo";
	}
	
	@PreAuthorize("hasAnyAuthority('COMPANY')")
	@PostMapping("/new")
	public String insertPromotion(@RequestParam(name = "image_promotion", required = true) MultipartFile image, @Valid @ModelAttribute("promotion") Promotions promotion,
			BindingResult result, Model model, RedirectAttributes attributes) {
		try {


			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Companies user = companiesRepository.findByEmail(auth.getName());
			model.addAttribute("user", user);
			model.addAttribute("userType", "empresa");
			
			if(result.hasErrors()) {
				model.addAttribute("promotion",promotion);
				return "/company/promotion/nuevo";
			}else {
				if(promotion.getOfertPrice().compareTo(promotion.getRegularPrice()) > 0) {
					result.addError(new ObjectError("ofertPrice", "El precio de oferta debe ser menor al regular"));
				}
				
				LocalDate initDate = promotion.getInitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
						endDate = promotion.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
						limitDate = promotion.getLimitDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				if(!endDate.isAfter(initDate)) {
					result.addError(new ObjectError("endDate", "Fecha de finalización debe ser mayor a la inicial"));
				}
				
				if(!limitDate.isAfter(endDate)) {
					result.addError(new ObjectError("limitDate", "Fecha limite debe ser mayor a la final"));
				}
				
				if(result.hasErrors()) {
					System.out.println("Errores");
					System.out.println(result.getAllErrors());
					model.addAttribute("promotion", promotion);
					return "/company/promotion/nuevo";
				}
				
				
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

				attributes.addFlashAttribute("confirmMsg", "Promoción registrada exitosamente");
				return "redirect:/promotion/list_company";
			}
		}catch(Exception ex) {
			attributes.addFlashAttribute("errorMsg", "Ha ocurrido un error en el proceso");
			model.addAttribute("promotion", promotion);
			return "/company/promotion/nuevo";	
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
			BindingResult result, Model model, RedirectAttributes attributes) {
		
		try {
			if(result.hasErrors()) {
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
						
						attributes.addFlashAttribute("confirmMsg", "Promoción modificada exitosamente");
						return "redirect:/promotion/list_company";
					}
				}
				return "redirect:/promotion/edit/"+promotion.getId();
			}
		}catch(Exception ex) {
			attributes.addFlashAttribute("errorMsg", "Ha ocurrido un error en el proceso");
			model.addAttribute("promotion", promotion);
			return "redirect:/promotion/edit/"+promotion.getId();			
		}
	}
	
	/*@PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
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
			
			return ResponseEntity.status(500).body("La promoción no cumple con los parametros para ser aprobada");
		}catch(Exception ex) {
			return ResponseEntity.status(500).body("Error en el proceso de aprobación");
		}
	}*/
	
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
	
	public String generateCode(Companies company) {
        try {
            int cuenta = 0;
            String codigo = "";
            
            cuenta = Integer.parseInt(this.salesService.count(company.getId()));
            
            if (cuenta > 0) {
                cuenta += 1;
                if (cuenta < 10) {
                    codigo = company.getId() + "000000" + cuenta;
                } else if (cuenta >= 10 && cuenta < 100) {
                    codigo = company.getId() + "00000" + cuenta;
                } else if (cuenta >= 100 && cuenta < 1000) {
                    codigo = company.getId() + "0000" + cuenta;
                } else if (cuenta >= 1000 && cuenta < 10000) {
                    codigo = company.getId() + "000" + cuenta;
                } else if (cuenta >= 10000 && cuenta < 100000) {
                    codigo = company.getId() + "00" + cuenta;
                } else if (cuenta >= 100000 && cuenta < 1000000) {
                    codigo = company.getId() + "0" + cuenta;
                } else {
                    codigo = company.getId() + cuenta;
                }
            } else {
                codigo = company.getId() + "0000001";
            }
            
            return codigo;
        } catch (Exception ex) {
            return null;
        }
    }
}
