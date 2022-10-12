package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private String className = null;
    private EntityClassMetaData classMetaData = null;

    public EntitySQLMetaDataImpl(String className) {
        this.className = className;
        this.classMetaData = new EntityClassMetaDataImpl(className);
    }

    @Override
    public String getSelectAllSql() {
        String result = "";
        try {
            String tableName = classMetaData.getName();
            List<Field> fields = classMetaData.getAllFields();
            result = "select ";
            for (Field field: fields) {
                result = result + (result.equals("") ? "" : ",") + field.getName();
            }
            result += " from " + tableName;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    public String getSelectByIdSql() {
        String result = "";
        try {
            String tableName = classMetaData.getName();
            List<Field> fields = classMetaData.getAllFields();
            result = "";
            for (Field field: fields) {
                result = result + (result.equals("") ? "" : ",") + field.getName();
            }
            result = "select " + result + " from " + tableName + " where " + classMetaData.getIdField().getName() + " = ?";
        }
        catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    public String getInsertSql() {
        String result = "";
        try {
            String fieldNames = "";
            String params = "";

            String tableName = classMetaData.getName();
            List<Field> fields = classMetaData.getFieldsWithoutId();
            for (Field field: fields) {
                fieldNames = fieldNames + (fieldNames.equals("") ? "" : ",") + field.getName();
                params = params + (params.equals("") ? "" : ",") + "?";
            }
            result = "insert into " + tableName + "(" + fieldNames + ") values(" + params + ")";
        }
        catch (Exception e) {
                e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    public String getUpdateSql() {
        String result = "";
        try {
            String tableName = classMetaData.getName();
            List<Field> fields = classMetaData.getAllFields();
            result = "update " + tableName + " set ";
            for (Field field: fields) {
                result += result.equals("") ? "" : "," + field.getName() + " = ?";
            }
            result += " where " + classMetaData.getIdField().getName() + " = ?";
        }
        catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }
}
