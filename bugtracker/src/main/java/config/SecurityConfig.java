package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(getPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/images/**").permitAll()
			.antMatchers("/", "/home", "/hello").authenticated()
			.anyRequest().authenticated()
			.and()
        .csrf().disable()
	    .formLogin()
	    	.loginPage("/login")
	    	.successForwardUrl( "/user/dispatch" )
	    	//.failureForwardUrl("/error")
        	.permitAll()
        	.and()
        .logout()
        	.permitAll();
	}
	
	private PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return charSequence.toString();
			}
			
			@Override
			public boolean matches(CharSequence charSequence, String s) {
				return true;
			}
		};
	}
}
