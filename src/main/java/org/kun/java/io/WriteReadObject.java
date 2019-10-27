package org.kun.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author kun
 * @date 2019/09/25
 */
public class WriteReadObject {

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        Person person = new Person("Jack", 15);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:\\person.txt"))) {
            oos.writeObject(person);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:\\person.txt"))) {
            Person person2 = (Person)ois.readObject();
            System.out.println(person2.getName() + person2.getAge());
        }

    }

}

class Person implements Serializable {

    private String name;
    private transient int age;

    /**
     * @param name
     * @param age
     */
    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

}
