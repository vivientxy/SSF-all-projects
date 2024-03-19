package sg.edu.nus.iss.day15demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day15demo.models.Person;
import sg.edu.nus.iss.day15demo.repositories.PersonRepo;

@Service
public class PersonService {
    
    @Autowired
    PersonRepo personRepo;

    public void addPerson(String key, Person person) {
        personRepo.addToList(key, person.toString());
    }

    public List<Person> getPersonList(String key) {
        List<String> rawList = personRepo.getList(key);
        List<Person> persons = new ArrayList<>();
        for (String string : rawList) {
            String[] attributes = string.split(",");
            Person p = new Person();
            p.setId(Integer.parseInt(attributes[0]));
            p.setFullname(attributes[1]);
            p.setSalary(Integer.parseInt(attributes[2]));
            persons.add(p);
        }
        return persons;
    }

    public void updatePerson(String key, Person originalPerson, Person updatedPerson) {
        String oldPersonString = originalPerson.toString();
        String newPersonString = updatedPerson.toString();
        personRepo.updateList(key, oldPersonString, newPersonString);
    }

    public void deletePerson(String key, Person person) {
        personRepo.deleteItem(key, person.toString());
    }

}
