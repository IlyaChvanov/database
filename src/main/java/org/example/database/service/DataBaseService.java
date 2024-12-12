package org.example.database.service;

import lombok.AllArgsConstructor;
import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class DataBaseService {
    DataBase dataBase;
    public Student AddStudent(int id, String name, int course, String dateOfBirth, String major) {
        LocalDate parsedDate = LocalDate.parse(dateOfBirth);
        Student student = new Student(id, name, course, parsedDate, Major.valueOf(major));
        if (dataBase.addStudent(student)) {
            return student;
        } else {
            throw new RuntimeException("Couldn't add student");
        }
    }
}

