package sv.edu.udb.www.cuponera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootApplication
public class CuponeraApplication {
	
	public static void main(String[] args) {
		String password = "123456";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println(hashedPassword);
		SpringApplication.run(CuponeraApplication.class, args);
	}
}
