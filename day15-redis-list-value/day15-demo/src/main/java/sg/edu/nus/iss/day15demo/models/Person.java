package sg.edu.nus.iss.day15demo.models;

public class Person {

    private int id;
    private String fullname;
    private int salary;
    
    public Person() {
    }
    public Person(int id, String fullname, int salary) {
        this.id = id;
        this.fullname = fullname;
        this.salary = salary;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return id + "," + fullname + "," + salary;
    }
}
