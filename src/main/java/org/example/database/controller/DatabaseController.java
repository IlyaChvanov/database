package org.example.database.controller;

import lombok.AllArgsConstructor;
import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
public class DatabaseController {
    DataBase dataBase;
    @PostMapping("/addStudent")
    public String addStudent(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("course") int course,
                             @RequestParam("dateOfBirth") String dateOfBirth,
                             @RequestParam("major") String major) {
        LocalDate parsedDate = LocalDate.parse(dateOfBirth);
        Student student = new Student(id, name, course, parsedDate, Major.valueOf(major));
        if (dataBase.addStudent(student)) {
            return "Student " + name + " added successfully!";
        } else {
            return "Student could not be added!";
        }
    }
}
