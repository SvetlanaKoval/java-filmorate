package ru.yandex.practicum.filmorate.dao;

import java.util.Collections;

public class BaseQueries {
    public static String getAll(String tableName) {
        return String.format("SELECT * FROM %s", tableName);
    }

    public static String getById(String tableName) {
        return String.format("SELECT * FROM %s WHERE id = ?", tableName);
    }

    public static String getAllById(String tableName, int size) {
        String dynamicParametersCount = String.join(",", Collections.nCopies(size, "?"));
        return String.format("SELECT * FROM %s WHERE id in (%s)", tableName, dynamicParametersCount);
    }

    public static String getByColum(String tableName, String columnName) {
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, columnName);
    }

    public static String insert(String tableName, String... columns) {
        String columnsNames = String.join(",", columns);
        String dynamicParameters = String.join(",", Collections.nCopies(columns.length, "?"));
        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnsNames, dynamicParameters);
    }

    public static String update(String tableName, String... updatingColumns) {
        String updatingQuery = String.join(" = ?, ", updatingColumns);
        return String.format("UPDATE %s set %s = ? WHERE id = ?", tableName, updatingQuery);
    }

    public static String delete(String tableName) {
        return String.format("DELETE FROM %s WHERE id = ?", tableName);
    }

    public static String deleteByField(String tableName, String fieldName) {
        return String.format("DELETE FROM %s WHERE %s = ?", tableName, fieldName);
    }

}