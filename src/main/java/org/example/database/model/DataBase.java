package org.example.database.model;

import lombok.Data;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;


@Data
public class DataBase {
    private File dbFile;
    private static DataBase instance = null;

    private Student getStudentFromDBEntry(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid database entry: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        int course = Integer.parseInt(parts[2]);
        LocalDate dateOfBirth = LocalDate.parse(parts[3]);
        Major major = Major.valueOf(parts[4]);
        return new Student(id, name, course, dateOfBirth, major);
    }

    public Optional<Student> findStudentByID(int id) {
        try (FileReader fr = new FileReader(dbFile)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(String.valueOf(id))) {
                    return Optional.of(getStudentFromDBEntry(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Student> findStudentByName(String name) {
        try (FileReader fr = new FileReader(dbFile)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(String.valueOf(name))) {
                    return Optional.of(getStudentFromDBEntry(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

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
        if (findStudentByID(student.getId()).isPresent()) {
            return false;
        }
        try (FileWriter fileOutputStream = new FileWriter(dbFile)) {
            StringBuilder studentData = new StringBuilder();
            studentData.append(student.getId()).append(" ")
                    .append(student.getName()).append(" ")
                    .append(student.getCourse()).append(" ")
                    .append(student.getDateOfBirth()).append(" ")
                    .append(student.getMajor()).append(" ");
            fileOutputStream.write(studentData.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
