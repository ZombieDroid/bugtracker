package bugtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity //Very important!
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		//the pages does not require login
		 http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();
		 
		//resources
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**").permitAll();
		http.authorizeRequests().antMatchers("/resources/**").permitAll();
		 
		//config for login form
		http.authorizeRequests().and().formLogin()
			.loginPage("/login")
        	/*.successForwardUrl( "/index" )
        	.failureForwardUrl("/error")*/
			.defaultSuccessUrl("/index")
			.failureUrl("/login?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
		//config for logout page
        .and()
        	.logout()
        	//.logoutUrl("/logout")
        	//.logoutSuccessUrl("/logoutSuccessful")
		;
	}
	
	@Autowired
	UserDetailsService userDetailsService;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(getPasswordEncoder());
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
