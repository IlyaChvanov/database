package org.example.database;


import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    DataBase dataBase = DataBase.getInstance();

    @AfterAll
    public static void tearDown() {
        DataBase dataBase = DataBase.getInstance();
        File dbFile = dataBase.getDbFile();
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            if (!lines.isEmpty()) {
                lines.remove(lines.size() - 1);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile, false))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Last line has been removed before the test.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testClassConnectedToFile() throws IOException {
        StringBuilder fileContent = new StringBuilder();
        FileWriter fileWriter = new FileWriter(dataBase.getDbFile());
        fileWriter.write("Hello World");
        fileWriter.close();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataBase.getDbFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Hello World", fileContent.toString());
    }

    @Test
    void testAddingStudent() {
        dataBase.addStudent(new Student(1, "vasya", 1, LocalDate.of(2000, 5, 22), Major.ART));

        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataBase.getDbFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals("1 vasya 1 2000-05-22 ART ", fileContent.toString());
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

}
