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
            return (dataBaseService.AddStudent(id, name, course, dateOfBirth, major).toString()) + "Added";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
