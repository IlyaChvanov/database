package org.example.database;


import org.example.database.model.DataBase;
import org.example.database.model.Major;
import org.example.database.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    DataBase dataBase = DataBase.getInstance();
    @BeforeEach
    public void setUp() {
        try (FileWriter fileWriter = new FileWriter(dataBase.getDbFile(), false)) {
            fileWriter.write("");  // Очистить файл перед каждым тестом
            System.out.println("File has been cleaned up before the test.");
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
}
