package org.example.database.model;

import lombok.Data;

import java.io.*;
import java.nio.file.Paths;

@Data
public class DataBase {
    private File dbFile;
    private static DataBase instance = null;
    private DataBase() {
        this.dbFile = Paths.get("src/main/resources", "maindb").toFile();
    }

    static public DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }
    public boolean addStudent(Student student) {
        System.out.println("Adding student: " + student.toString());
        try(FileWriter fileOutputStream = new FileWriter(dbFile)) {
            StringBuilder studentData = new StringBuilder();
            studentData.append(student.getId()).append(" ")
                    .append(student.getName()).append(" ")
                    .append(student.getCourse()).append(" ")
                    .append(student.getDateOfBirth()).append(" ")
                    .append(student.getMajor()).append(" ");
            System.out.println(studentData);
            fileOutputStream.write(studentData.toString());
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
