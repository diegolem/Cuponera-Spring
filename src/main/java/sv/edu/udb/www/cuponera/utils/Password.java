package sv.edu.udb.www.cuponera.utils;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {
	public static String RandomPassword(int size) {
		
		String password = "";
		
		Random rnd = new Random();

		for (int i = 0; i < size; i++) password += (char)(rnd.nextInt(91) + 65);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}
}
