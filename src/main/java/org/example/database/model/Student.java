package org.example.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
public class Student {
    private int id;
    private String name;
    private int course;
    private LocalDate dateOfBirth;
    private Major major;
}
