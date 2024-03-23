package nus.iss.day17weatherapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nus.iss.day17weatherapi.config.AppConfig;

@SpringBootApplication
public class Day17WeatherApiApplication implements CommandLineRunner {

	@Autowired
	AppConfig appConfig;

	public static void main(String[] args) {
		SpringApplication.run(Day17WeatherApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Weather weather = svc.getWeatherAPI("singapore");
		// System.out.println("WEATHER >>>>>>> " + weather.toString());
	}

}
