package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.FileHandler;

public class FileSerializer implements Serializer {
    private String filePath = "";

    public FileSerializer(String fileName) {
        this.filePath = Paths.get(fileName).toString();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        Gson gson = new Gson();

        try {
            FileWriter fw = new FileWriter(filePath);
            gson.toJson(data, fw);
            fw.flush();
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
