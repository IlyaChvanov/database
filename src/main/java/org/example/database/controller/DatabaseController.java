package org.example.database.controller;

import lombok.AllArgsConstructor;
import org.example.database.service.DataBaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class DatabaseController {
    DataBaseService dataBaseService;

    @PostMapping("/addStudent")
    public String addStudent(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("course") int course,
                             @RequestParam("dateOfBirth") String dateOfBirth,
                             @RequestParam("major") String major) {
        try {
            return (dataBaseService.AddStudent(id, name, course, dateOfBirth, major).toString()) + " Added";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    @PostMapping("/clearDatabase")
    public String clearDatabase() {
        dataBaseService.clearDatabase();
        return "Database has been cleared";
    }

    @PostMapping("/deleteStudentById")
    public String deleteStudentById(@RequestParam("id") int id) {
        dataBaseService.deleteStudentById(id);
        return "Student is deleted with id: " + id;
    }

    @PostMapping("/deleteStudentByName")
    public String deleteStudentByName(@RequestParam("name") String name) {
        dataBaseService.deleteStudentByName(name);
        return "Student is deleted with name: " + name;
    }
}
