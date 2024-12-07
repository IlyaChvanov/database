package org.example.database.model;

import lombok.Data;

import java.io.*;
import java.nio.file.Paths;

@Data
public class DataBase {
    private File dbFile;
    private DataBase() {
        try {
            this.dbFile = Paths.get("src/main/resources", "maindb").toFile();
            FileWriter fileOutputStream = new FileWriter(dbFile);
            fileOutputStream.write("Hello World");
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static public DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }
    private static DataBase instance = null;
}
