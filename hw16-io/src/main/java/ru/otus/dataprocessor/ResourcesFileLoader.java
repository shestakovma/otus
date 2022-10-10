package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.otus.model.Measurement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;

public class ResourcesFileLoader implements Loader {
    private String filePath = "";
    private static final Type measurementType = new TypeToken<List<Measurement>>() {}.getType();

    public ResourcesFileLoader(String fileName) {
        this.filePath = FileRoutines.resourcePathToFilePath(fileName);
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        List<Measurement> result = null;

        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(filePath));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        result = gson.fromJson(reader, measurementType);
        return result;
    }
}
