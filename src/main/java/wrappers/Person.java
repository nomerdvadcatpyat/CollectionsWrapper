package wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person implements Serializable{
    private String name;
    private List<Person> relatives;

    public Person() {
    }

    public Person(String name, List<Person> relatives){
        this.name = name;
        this.relatives = relatives;
    }

    public Person(String name){
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


    @Override
    public String toString() {
        return String.format("Name: %s, Date of Birth:, relatives: "+ relatives , name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) &&
                Objects.equals(relatives, person.relatives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relatives);
    }
}