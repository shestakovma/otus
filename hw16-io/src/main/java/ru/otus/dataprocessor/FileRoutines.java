package ru.otus.dataprocessor;

import java.nio.file.LinkOption;
import java.nio.file.Paths;

public class FileRoutines {
    public static String resourcePathToFilePath(String resourcePath) {
        String  path = "";
        try {
            var uri = ClassLoader.getSystemResource(resourcePath).toURI();
            path = Paths.get(uri).toRealPath(new LinkOption[0]).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String shortPathToFilePath(String shortPath) {
        String  path = "";
        try {
            path = Paths.get(shortPath).toRealPath(new LinkOption[0]).toString();;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
