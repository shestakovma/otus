package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String query = entitySQLMetaData.getSelectByIdSql();

        return dbExecutor.executeSelect(connection, query, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    Constructor<T> constructor = entityClassMetaData.getConstructor();
                    T object = constructor.newInstance();
                    List<Field> fields = entityClassMetaData.getAllFields();

                    for (Field field: fields) {
                        setValue(object, field, rs);
                    }
                    return object;
                }
                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String query = entitySQLMetaData.getSelectAllSql();

        return dbExecutor.executeSelect(connection, query, Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    Constructor<T> constructor = entityClassMetaData.getConstructor();
                    T object = constructor.newInstance();
                    List<Field> fields = entityClassMetaData.getFieldsWithoutId();

                    for (Field field : fields) {
                        setValue(object, field, rs);
                    }
                    for (Field field: fields) {
                        setValue(object, field, rs);
                    }

                    clientList.add(object);
                }
                return clientList;
            }
            catch (Exception e) {
                    e.printStackTrace();
                }
            return null;
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        String query = entitySQLMetaData.getInsertSql();

        try {
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            List<Object> params = new ArrayList<>();
            for (Field field: fields) {
                params.add(getValue(client, field));
            }
            fields.stream().map(field -> field.getName()).toList();
            return dbExecutor.executeStatement(connection, query, params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        throw new UnsupportedOperationException();
    }

    private void setValue(T object, Field field, ResultSet resultSet) {
        field.setAccessible(true);
        Class<?> t = field.getType();
        try {
            if (t.equals(Long.class)) {
                field.set(object, resultSet.getLong(field.getName()));
            }
            else if (t.equals(String.class)) {
                field.set(object, resultSet.getString(field.getName()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Object getValue(T object, Field field) {
        Object result = null;
        try {
            field.setAccessible(true);
            result = field.get(object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
