package sv.edu.udb.www.cuponera.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import sv.edu.udb.www.cuponera.entities.Employees;
import sv.edu.udb.www.cuponera.entities.Sales;
import sv.edu.udb.www.cuponera.entities.SalesState;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.entities.simple.SimpleSales;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
import sv.edu.udb.www.cuponera.repositories.SalesRepository;
import sv.edu.udb.www.cuponera.repositories.SalesStateRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@RestController
@RequestMapping("/employee_rest")
public class EmployeesRestController {
	
	
	@Autowired
	@Qualifier("UsersRepository")
	UsersRepository userRepository;
	
	@Autowired
	@Qualifier("SalesRepository")
	SalesRepository salesRepository;
	
	@Autowired
	@Qualifier("EmployeesRepository")
	EmployeesRepository employeesRepository;
	
	@Autowired
	@Qualifier("SalesStateRepository")
	SalesStateRepository salesStateRepository;
	
	@RequestMapping(value = {"/promotions/approve/{code}"}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String AceptSale(@PathVariable("code") String code){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			if( this.salesRepository.existCupon(code) ) {
				
				Optional<SalesState> state = this.salesStateRepository.findById(1);
				
				if	(state.isPresent())  {
				
					Sales sales = this.salesRepository.findByCouponCode(code);
					sales.setState(state.get());
					
					this.salesRepository.saveAndFlush(sales);
					
					data.put("state", true);
					
				} else {
					data.put("state", false);
					data.put("error", "Nos se puede aceptar");
				}
				
			} else {
				data.put("state", false);
				data.put("error", "No existe el cliente");
			}
		} catch(Exception error) {
			data.put("state", false);
			data.put("error", error.getMessage());
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
	@RequestMapping(value = {"/promotions"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public @ResponseBody String GetPromotionByDui(@RequestParam("dui") String dui){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();
		
		try {
			if( this.userRepository.existsDuiOfClient(dui) ) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
				/*
				Employees employee = this.employeesRepository.findByEmail(auth.getName());
				String idCompany =  employee.getCompany().getId();
				*/
				
				Users client = this.userRepository.findByDui(dui);
				
				int idUser = client.getId();
				
				data.put("state", true);
				data.put("sales", SimpleSales.Parse(this.salesRepository.findByIdClient(idUser, "ABC123")));
				data.put("history", SimpleSales.Parse(this.salesRepository.findByIdClientHistory(idUser, "ABC123")));
				
			} else {
				data.put("state", false);
				data.put("error", "No existe el cliente");
			}
		} catch(Exception error) {
			data.put("state", false);
			data.put("error", error.getMessage());
		}
		
		try {
			return mapper.writeValueAsString(data);
		} catch(Exception error) {
			return error.getMessage();
		}
	}
	
}
