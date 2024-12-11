package org.example.database;


import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    DataBase dataBase = DataBase.getInstance();

    @AfterEach
    public void tearDown() {
        DataBase dataBase = DataBase.getInstance();
        try (FileWriter writer = new FileWriter(dataBase.getDbFile()) ){
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testClassConnectedToFile() throws IOException {
        StringBuilder fileContent = new StringBuilder();
        FileWriter fileWriter = new FileWriter(dataBase.getDbFile());
        fileWriter.write("Hello from test of connection");
        fileWriter.flush();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataBase.getDbFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Hello from test of connection", fileContent.toString());
    }

    @Test
    void testAddStudent() {
        dataBase.addStudent(new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART));
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataBase.getDbFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            assertEquals("1 vasya 1 2000-05-22 ART", fileContent.toString());

            dataBase.addStudent(new Student(2, "vasya2", 1, LocalDate.of(2000, 5, 22), Major.ART));
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            assertEquals("1 vasya 1 2000-05-22 ART2 vasya2 1 2000-05-22 ART", fileContent.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindStudentById() {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        assertEquals(vasya, dataBase.findStudentByID(vasya.getId()).get());
        assertEquals(Optional.empty(), dataBase.findStudentByID(-1));
    }

    @Test
    void testFindStudentByName() {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        assertEquals(vasya, dataBase.findStudentByName(vasya.getName()).get());
        assertEquals(Optional.empty(), dataBase.findStudentByName("UNREAL NAME"));
    }

    @Test
    void testGetAllStudents() {
        Student vasya = new Student(1, "vasya1", 1, LocalDate.of(2000, 5, 22), Major.ART);
        Student vasya1 = new Student(2, "vasya2", 1, LocalDate.of(2000, 5, 22), Major.ART);
        Student vasya2 = new Student(3, "vasya3", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        dataBase.addStudent(vasya1);
        dataBase.addStudent(vasya2);

        List<Student> requiredResponse = new ArrayList<Student>(List.of(vasya, vasya1, vasya2));
        assertEquals(requiredResponse, dataBase.getAllStudents());
    }

    @Test
    void testDeleteStudentById() {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        assertEquals(vasya, dataBase.findStudentByID(vasya.getId()).get());

        dataBase.deleteStudentById(vasya.getId());
        assertEquals(Optional.empty(), dataBase.findStudentByID(vasya.getId()));
    }

    @Test
    void testDeleteStudentByName() {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        assertEquals(vasya, dataBase.findStudentByName(vasya.getName()).get());

        dataBase.deleteStudentByName(vasya.getName());
        assertEquals(Optional.empty(), dataBase.findStudentByName(vasya.getName()));
    }

    @Test
    void testUpdateStudent() throws CloneNotSupportedException {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        Student vasya1 = vasya.clone();
        vasya1.setName("vasya1");
        dataBase.ChangeStudent(vasya1);

        assertEquals(vasya1, dataBase.findStudentByID(vasya.getId()).get());
    }

    @Test
    void testCreateBackup() throws IOException {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        
        dataBase.CreateBackup();
        try (FileReader reader = new FileReader(Paths.get("src", "main", "resources", "backup").toFile());
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            line = bufferedReader.readLine();
            if (line != null) {
                System.out.println("Первая строка файла: " + line);
            } else {
                System.out.println("Файл пуст.");
            }
            assertEquals("1 vasya 1 2000-05-22 ART", line);
            FileWriter writer = new FileWriter(Paths.get("src", "main", "resources", "backup").toFile());
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRestoreFromBackup() {
        Student vasya = new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART);
        dataBase.addStudent(vasya);
        dataBase.CreateBackup();

        dataBase.ClearTable();
        dataBase.RestoreBackup();

        assertEquals(vasya, dataBase.findStudentByID(vasya.getId()).get());
    }
}
