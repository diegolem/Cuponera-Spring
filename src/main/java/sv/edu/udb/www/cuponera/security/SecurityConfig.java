package sv.edu.udb.www.cuponera.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Qualifier;
import sv.edu.udb.www.cuponera.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("UserDetailsServiceImpl")
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/coupons", "/coupons/**", "/coupon/**").anonymous()
				.antMatchers("/css/**", "/js/**", "/font/**", "/images/**", "/jasper/**", "/promotions_images/**", "/scss/**", "/vendors/**", "/").permitAll()
				.anyRequest().authenticated()
				// .antMatchers("/admin").hasAuthority("ADMINISTRATOR")
				// .antMatchers("/client").hasAuthority("CLIENT")
				// .antMatchers("/companies").hasAnyAuthority("COMPANY")
				// .antMatchers("/employees").hasAnyAuthority("EMPLOYEE")
				// .antMatchers("/promotion/list_company").hasAnyAuthority("COMPANY")
				// .antMatchers("/promotion/new").hasAnyAuthority("COMPANY")
				// .antMatchers("/promotion/list_admin").hasAnyAuthority("ADMINISTRATOR")
				.and()
			.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginPage("/login")
				.successForwardUrl("/loginsucess")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login")
				.permitAll()
				.and()
			.exceptionHandling().accessDeniedPage("/denied");
		;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/recover_password").and().ignoring().antMatchers("/users/exist_mail");
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/*public PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return true;
			}
		};
	}*/
}