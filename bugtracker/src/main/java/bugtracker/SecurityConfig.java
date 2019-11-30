package bugtracker;

import bugtracker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity //Very important!
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	UserService userService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {



		httpSecurity.authorizeRequests()
				.antMatchers("/login*").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER') or hasRole('ROLE_APPROVER')")
				.antMatchers("/user/new").access(" hasRole('ROLE_ADMIN')")
				.antMatchers("/user/all*").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER') or hasRole('ROLE_APPROVER')")
				.antMatchers("/user/modify*").access("hasRole('ROLE_ADMIN')")
				.antMatchers("/project/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER') or hasRole('ROLE_APPROVER')")
				.antMatchers("/api/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER') or hasRole('ROLE_APPROVER')")
				.antMatchers("/ticket/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER') or hasRole('ROLE_APPROVER')")
				.and()
				.csrf().disable()
				.formLogin()
				.loginPage("/login")
				.successForwardUrl("/api/user/dispatch")
				.permitAll()
				.and()
				.logout()
				.permitAll();
		httpSecurity.userDetailsService(userService);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
				.passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
		return new AppAuthenticationSuccessHandler();
	}

}

class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	protected void handle(HttpServletRequest request, HttpServletResponse response,
						  Authentication authentication) throws IOException, ServletException {
	}

}
