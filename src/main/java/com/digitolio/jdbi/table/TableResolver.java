package com.digitolio.jdbi.table;

import com.digitolio.jdbi.annotations.PK;
import com.digitolio.jdbi.strategy.TranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author C.Koc
 */
public class TableResolver {

    public Table resolve(Class<?> type, TranslatingStrategyAware strategy) {

        List<Field> list = getInheritedFields(type);
        TranslatingStrategy fieldTranslatingStrategy = strategy.getPropertyTranslatingStrategy();

        // columns
        List<Column> allColumns = new ArrayList<Column>();
        for (Field field : list) {
            allColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName())));
        }

        // pk
        List<Field> pkFields = new ArrayList<Field>();
        for (Field field : list) {
            if (field.getAnnotation(PK.class) != null) {
                pkFields.add(field);
            }
        }

        // columns
        List<Column> pkColumns = new ArrayList<Column>();
        for (Field field : pkFields) {
            pkColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName())));
        }

        com.digitolio.jdbi.annotations.Table tableAnnotation = type.getAnnotation(com.digitolio.jdbi.annotations.Table.class);
        String tableName = tableAnnotation != null ?
                tableAnnotation.name() :
                fieldTranslatingStrategy.translate(type.getSimpleName());

        return new Table(tableName, allColumns, pkColumns);
    }

    public List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
