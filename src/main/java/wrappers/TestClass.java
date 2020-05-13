package wrappers;

import java.io.Serializable;
import java.util.Objects;

public class TestClass implements Serializable{
    private String title;

    public TestClass(String title){
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return String.format("Title: %s", title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClass testClass = (TestClass) o;
        return title.equals(testClass.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}