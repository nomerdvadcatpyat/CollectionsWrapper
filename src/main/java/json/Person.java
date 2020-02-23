package json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person {
    private String name;
    private Date dob;
    private List<Person> relatives;

    public Person(String name, Date date_of_birth,List<Person> relatives){
        dob = date_of_birth;
        this.name = name;
        this.relatives = relatives;
    }

    public Person(String name, Date date_of_birth){
        dob = date_of_birth;
        this.name = name;
        relatives = new ArrayList<>();
    }

    public List<Person> getRelatives(){
        return relatives;
    }

    public void addRelatives(Person... names){
        for (Person p:
             names) {
            relatives.add(p);
        }
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Date of Birth: %tD", name, dob);
    }
}