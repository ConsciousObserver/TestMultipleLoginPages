package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
public class TestMultipleLoginPagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestMultipleLoginPagesApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

@Controller
class MvcController {
	@RequestMapping(path="admin/adminLogin", method=RequestMethod.GET)
	public String adminLoginPage() {
		return "adminLogin";
	}
	
	@RequestMapping(path="admin/adminHome", method=RequestMethod.GET)
	public String adminHomePage() {
		return "adminHome";
	}
	
	@RequestMapping(path="user/userLogin", method=RequestMethod.GET)
	public String userLoginPage() {
		return "userLogin";
	}
	
	@RequestMapping(path="user/userHome", method=RequestMethod.GET)
	public String userHomePage() {
		return "userHome";
	}
}

@Configuration
@Order(1)
class AdminSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/admin/**")
			.authorizeRequests()
				.anyRequest().hasRole("ADMIN")
			.and()
			.formLogin()
				.loginPage("/admin/adminLogin").permitAll()
				.loginProcessingUrl("/admin/adminLoginPost").permitAll()
				.defaultSuccessUrl("/admin/adminHome")
			.and()
			.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/admin/adminLogin")
			.and()
			.httpBasic().disable()
			.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder)
			.withUser("admin")
			.password(passwordEncoder.encode("test"))
			.roles("ADMIN", "USER");
	}
}

@Configuration
@Order(2)
class UserSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
			.authorizeRequests()
				.anyRequest().hasRole("USER")
			.and()
			.formLogin()
				.loginPage("/user/userLogin").permitAll()
				.loginProcessingUrl("/user/userLoginPost").permitAll()
				.defaultSuccessUrl("/user/userHome")
				.and()
				.logout()
					.logoutUrl("/user/logout")
					.logoutSuccessUrl("/user/userHome")
				.and()
				.httpBasic().disable()
				.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder)
			.withUser("user")
			.password(passwordEncoder.encode("test"))
			.roles("USER");
	}
}