package service;

import java.util.*;
import model.Student;

public class StudentService {
    private List<Student> list = new ArrayList<>();

    public void add(Student s) {
        list.add(s);
    }

    public void delete(String id) {
        list.removeIf(s -> s.getId().equals(id));
    }

    public void update(String id, Student newData) {
        for (Student s : list) {
            if (s.getId().equals(id)) {
                s.setName(newData.getName());
                s.setClassName(newData.getClassName());
                s.setAge(newData.getAge());
            }
        }
    }

    public List<Student> getAll() {
        return list;
    }

    public List<Student> search(String keyword) {
        keyword = keyword.toLowerCase();
        List<Student> result = new ArrayList<>();

        for (Student s : list) {
            if (s.getId().toLowerCase().contains(keyword) ||
                s.getName().toLowerCase().contains(keyword) ||
                s.getClassName().toLowerCase().contains(keyword)) {
                result.add(s);
            }
        }
        return result;
    }

    public Student findById(String id) {
        return list.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}