package sv.edu.udb.www.cuponera.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sv.edu.udb.www.cuponera.entities.AllUsers;
import sv.edu.udb.www.cuponera.repositories.AllUsersRepository;
import sv.edu.udb.www.cuponera.repositories.CompaniesRepository;
import sv.edu.udb.www.cuponera.repositories.EmployeesRepository;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	@Qualifier("AllUsersRepository")
	private AllUsersRepository allUsersRepository;
	@Autowired
	@Qualifier("UsersRepository")
	private UsersRepository usersRepository;
	@Autowired
	@Qualifier("CompaniesRepository")
	private CompaniesRepository companiesRepository;
	@Autowired
	@Qualifier("EmployeesRepository")
	private EmployeesRepository employeesRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		AllUsers user = allUsersRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}else {
			if(user.getUserType().toLowerCase().equals("administrator")) {
				return new UserDetailsImpl(usersRepository.findByEmail(user.getEmail()));
			}else if (user.getUserType().toLowerCase().equals("client") && user.getConfirmed().equals("1")){
				return new UserDetailsImpl(usersRepository.findByEmail(user.getEmail()));
			}else if(user.getUserType().toLowerCase().equals("company")) {
				return new CompanyDetailsImpl(companiesRepository.findByEmail(user.getEmail()));
			}else if(user.getUserType().toLowerCase().equals("employee")) {
				return new EmployeeDetailsImpl(employeesRepository.findByEmail(user.getEmail()));
			}else {
				throw new UsernameNotFoundException(username); 
			}
		}
	}
}
