package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping(value="/")
	public String Demo(){
		return "Programming Hello World - version 3.2";
	}

        @RequestMapping(value="/about")
        public String About(){
                return "About Java SpringBoot";
        }


}
