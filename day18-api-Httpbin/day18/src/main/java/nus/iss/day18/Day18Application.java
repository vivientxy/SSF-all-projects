package nus.iss.day18;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nus.iss.day18.model.User;
import nus.iss.day18.service.HttpbinService;

@SpringBootApplication
public class Day18Application implements CommandLineRunner{

	@Autowired
	HttpbinService httpbinService;

	public static void main(String[] args) {
		SpringApplication.run(Day18Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User("barney", "barney@email.com");
		httpbinService.postByUrlEncodedForm(user);
		httpbinService.postByJson(user);
	}

}
