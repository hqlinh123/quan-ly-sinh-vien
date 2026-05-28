package model;

public class Student {
    private String id;
    private String name;
    private String className;
    private int age;

    public Student(String id, String name, String className, int age) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.age = age;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getClassName() { return className; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }
    public void setClassName(String className) { this.className = className; }
    public void setAge(int age) { this.age = age; }
}