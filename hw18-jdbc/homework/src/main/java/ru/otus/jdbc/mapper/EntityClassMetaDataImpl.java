package ru.otus.jdbc.mapper;

import ru.otus.crm.model.annotation.DbTable;
import ru.otus.crm.model.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl implements EntityClassMetaData {
    private String className = null;

    public EntityClassMetaDataImpl(String className) {
        this.className = className;
    }

    @Override
    public String getName() {
        String result = null;
        try {
            Class<?> currentClass = Class.forName(className);
            if (currentClass.isAnnotationPresent(DbTable.class)) {
                DbTable dbTable = currentClass.getAnnotation(DbTable.class);
                result = dbTable.name();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Constructor getConstructor() {
        Constructor<?> result = null;
        try {
            result = Class.forName(className).getConstructor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Field getIdField() {
        Field result = null;
        try {
            Class<?> currentClass = Class.forName(className);
            List<Field> fields = Arrays.stream(currentClass.getDeclaredFields()).toList();
            if (fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).count() > 0)
                result = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Field> getAllFields() {
        List<Field> result = null;
        try {
            Class<?> currentClass = Class.forName(className);
            result = Arrays.stream(currentClass.getDeclaredFields()).toList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> result = null;
        try {
            Class<?> currentClass = Class.forName(className);
            result = Arrays.stream(currentClass.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Id.class)).toList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
