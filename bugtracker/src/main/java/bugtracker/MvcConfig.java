package bugtracker;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/home").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/ticket/all").setViewName("tickets");
		registry.addViewController("/ticket").setViewName("ticket");
		registry.addViewController("/ticket/new").setViewName("new_ticket");
		registry.addViewController("/user/new").setViewName("new_user");
		registry.addViewController("/user/modify").setViewName("user");
		registry.addViewController("/user/all").setViewName("users");
		registry.addViewController("/status/new").setViewName("new_status");
		registry.addViewController("/project/new").setViewName("new_project");
		registry.addViewController("/project/all").setViewName("projects");
		registry.addViewController("/project").setViewName("project");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/* In any modern browser, the Cross-Origin Resource Sharing (CORS) is a relevant
		 * specification with the emergence of HTML5 and JS clients that consume data via REST APIs.
		 *
		 * https://www.baeldung.com/spring-cors
		 * enables CORS requests from any origin to any endpoint in the application
		 * */
        registry.addMapping("/**");
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
	    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
	}
}
