package org.example.database;


import org.example.database.model.DataBase;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    @Test
    void testClassConnectedToFile() throws FileNotFoundException {
        DataBase dataBase = DataBase.getInstance();
        FileReader fileReader = new FileReader(dataBase.getDbFile());
        StringBuilder fileContent = new StringBuilder();
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
}
