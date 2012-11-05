package com.digitolio.jdbi.table;

import com.digitolio.jdbi.annotations.PK;
import com.digitolio.jdbi.strategy.TranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author C.Koc
 */
public class TableResolver {

    public Table resolve(Class<?> type, TranslatingStrategyAware strategy) {

        List<Field> list = asList(type.getDeclaredFields());
        TranslatingStrategy fieldTranslatingStrategy = strategy.getPropertyTranslatingStrategy();

        // columns
        List<Column> allColumns = new ArrayList<Column>();
        for (Field field : list) {
            allColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName())));
        }

        // pk
        List<Field> pkFields = new ArrayList<Field>();
        for (Field field : list) {
            if(field.getAnnotation(PK.class) != null){
                pkFields.add(field);
            }
        }

        // columns
        List<Column> pkColumns = new ArrayList<Column>();
        for (Field field : pkFields) {
            pkColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName())));
        }


        String simpleName = fieldTranslatingStrategy.translate(type.getSimpleName());
        return new Table(simpleName, allColumns, pkColumns);
    }
}
