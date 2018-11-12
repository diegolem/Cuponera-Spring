package sv.edu.udb.www.cuponera.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import sv.edu.udb.www.cuponera.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/admin").hasAuthority("ADMINISTRATOR")
				.antMatchers("/client").hasAuthority("CLIENT")
				.antMatchers("/companies").hasAnyAuthority("COMPANY")
				.antMatchers("/employees").hasAnyAuthority("EMPLOYEE")
				.antMatchers("/promotion/list_company").hasAnyAuthority("COMPANY")
				.antMatchers("/promotion/new").hasAnyAuthority("COMPANY")
				.antMatchers("/promotion/list_admin").hasAnyAuthority("ADMINISTRATOR")
				.antMatchers("/admin/companies/").hasAuthority("ADMINISTRATOR")
				.antMatchers("/admin/company_type/").hasAuthority("ADMINISTRATOR")
				.antMatchers("/admin/users/").hasAuthority("ADMINISTRATOR")
				.antMatchers("/admin/promotions/").hasAuthority("ADMINISTRATOR")
				.antMatchers("/admin/configuration/").hasAuthority("ADMINISTRATOR")
				.antMatchers("/client/configuration/").hasAuthority("CLIENT")
				.antMatchers("/company/configuration/").hasAuthority("COMPANY")
				.antMatchers("/employee/configuration/").hasAuthority("EMPLOYEE")
				.antMatchers("/recover_password").anonymous()
				.and()
			.formLogin()
				.loginPage("/login")
				.successForwardUrl("/loginsucess")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/")
				.permitAll()
				.and()
			.exceptionHandling().accessDeniedPage("/denied")
		;
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}
	
	public PasswordEncoder getPasswordEncoder() {
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
	}
}