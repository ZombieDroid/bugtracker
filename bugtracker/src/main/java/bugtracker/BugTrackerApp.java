package bugtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class BugTrackerApp {

	public static void main(String[] args) {
		SpringApplication.run(BugTrackerApp.class, args);
		System.out.println("");
	}

}
