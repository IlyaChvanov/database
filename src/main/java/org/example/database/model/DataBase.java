package org.example.database.model;

import lombok.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Data
public class DataBase {
    private File dbFile;
    private File backupFile;
    private static DataBase instance = null;
    private DataBase() {
        this.dbFile = Paths.get("src/main/resources", "maindb").toFile();
    }

    private Student getStudentFromDBEntry(String line) {
        String[] parts = line.split(" ");
        if (parts.length != 5) {
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
                if (getStudentFromDBEntry(line).getId() == id) {
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
        try (FileWriter fileOutputStream = new FileWriter(dbFile, true)) {
            StringBuilder studentData = new StringBuilder();
            studentData.append(student.getId()).append(" ")
                    .append(student.getName()).append(" ")
                    .append(student.getCourse()).append(" ")
                    .append(student.getDateOfBirth()).append(" ")
                    .append(student.getMajor()).append(System.lineSeparator());
            fileOutputStream.write(studentData.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try(FileReader fr = new FileReader(dbFile)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                students.add(getStudentFromDBEntry(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void ClearTable() {
        try (FileWriter writer = new FileWriter(dbFile) ){
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudentById(int id) {
        List<Student> students = getAllStudents();
        students.removeIf(student -> student.getId() == id);
        ClearTable();
        students.forEach(this::addStudent);
    }
    public void deleteStudentByName(String name) {
        List<Student> students = getAllStudents();
        students.removeIf(student -> student.getName().equals(name));
        ClearTable();
        students.forEach(this::addStudent);
    }

    public void ChangeStudent(Student student) {
        List<Student> students = getAllStudents();
        if(students.removeIf(studentToChange -> studentToChange.getId() == student.getId())) {
            students.add(student);
            ClearTable();
            students.forEach(this::addStudent);
        } else {
            throw new IllegalArgumentException("Student not found");
        }
    }

    public void CreateBackup() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dbFile.getAbsolutePath()));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources", "backup"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine(); // добавляем новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RestoreBackup() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("src/main/resources/backup"));
             BufferedWriter writer = Files.newBufferedWriter(Path.of(dbFile.getAbsolutePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
