package org.example.database.service;

import lombok.AllArgsConstructor;
import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public void clearDatabase() {
        dataBase.clearTable();
    }

    public void deleteStudentById(int id) {
        dataBase.deleteStudentById(id);
    }
    public void deleteStudentByName(String name) {
        dataBase.deleteStudentByName(name);
    }

    public Student searchStudentById(int id) {
        return dataBase.findStudentByID(id).orElse(null);
    }
    public List<Student> searchStudentByName(String name) {
        return dataBase.findStudentByName(name);
    }

    public void editStudent(int id, String name, int course, String dateOfBirth, String major) {
        Student student = new Student(id, name, course, LocalDate.parse(dateOfBirth), Major.valueOf(major));
        dataBase.editStudent(student);
    }

    public void createBackup() {
        dataBase.createBackup();
    }
    public void restoreFromBackup() {
        dataBase.restoreBackup();
    }

    public List<Student> showAllStudents() {
        return dataBase.getAllStudents();
    }
}

