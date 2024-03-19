package sg.edu.nus.iss.day15demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.day15demo.models.Person;
import sg.edu.nus.iss.day15demo.repositories.TestRepo;
import sg.edu.nus.iss.day15demo.services.PersonService;
import sg.edu.nus.iss.day15demo.utils.Util;

@SpringBootApplication
public class Day15DemoApplication implements CommandLineRunner {

	@Autowired
	TestRepo testRepo;
	@Autowired
	PersonService personService;

	public static void main(String[] args) {
		SpringApplication.run(Day15DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		testRepo.storeValueData("count", "1000");
		String countValue = testRepo.retrieveValueData("count");
		System.out.println("Count from Redis: " + countValue);

		testRepo.storeValueData("email", "vivientxy@email.com");
		String email = testRepo.retrieveValueData("email");
		System.out.println("Email from Redis: " + email);

		testRepo.addToList("cart", "apple");
		testRepo.addToList("cart", "orange");
		testRepo.addToList("cart", "pear");
		List<String> fruits = testRepo.getList("cart");
		fruits.forEach(System.out::println);

		Person p = new Person(1, "ABC", 15000);
		personService.addPerson(Util.KEY_PERSON, p);
		p = new Person(2, "DEF", 20000);
		personService.addPerson(Util.KEY_PERSON, p);
		p = new Person(3, "GHI", 25000);
		personService.addPerson(Util.KEY_PERSON, p);

		List<Person> persons = personService.getPersonList(Util.KEY_PERSON);
		for (Person person : persons) {
			System.out.println(person.toString());
		}

		// // try update and delete too
		// System.out.println("BELOW IS TESTING THE UPDATE FUNCTIONS");

		// Person p2 = p;
		// p2.setFullname("Hello");
		// personService.updatePerson(Util.KEY_PERSON, p, p2);

		// persons = personService.getPersonList(Util.KEY_PERSON);
		// for (Person person : persons) {
		// 	System.out.println(person.toString());
		// }

		// System.out.println("BELOW IS TESTING THE DELETE FUNCTIONS");

		// personService.deletePerson(Util.KEY_PERSON, p2);

		// persons = personService.getPersonList(Util.KEY_PERSON);
		// for (Person person : persons) {
		// 	System.out.println(person.toString());
		// }
	}

}
