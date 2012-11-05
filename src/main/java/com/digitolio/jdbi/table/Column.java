package com.digitolio.jdbi.table;

import java.lang.reflect.Field;

/**
 * @author C.Koc
 */
public class Column {
    Field field;
    String databaseName;

    public Column(Field field, String databaseName) {
        this.field = field;
        this.databaseName = databaseName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getFieldName() {
        return field.getName();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
