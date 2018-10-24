package sv.edu.udb.www.cuponera.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sv.edu.udb.www.cuponera.entities.Users;
import sv.edu.udb.www.cuponera.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Users user = usersRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

}
