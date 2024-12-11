package org.example.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
public class Student implements Cloneable{
    private int id;
    private String name;
    private int course;
    private LocalDate dateOfBirth;
    private Major major;

    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }
}
