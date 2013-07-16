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
      List<Column> allColumns = new ArrayList<>();
      for(Field field : list) {

         com.digitolio.jdbi.annotations.Column annotation = field.getAnnotation(com.digitolio.jdbi.annotations.Column.class);
         boolean nullable = annotation != null ? annotation.nullable() : com.digitolio.jdbi.annotations.Column.defaultNullable;
         allColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName()), nullable));
      }

      // pk
      List<Field> pkFields = new ArrayList<>();
      for(Field field : list) {
         if(field.getAnnotation(PK.class) != null) {
            pkFields.add(field);
         }
      }

      // columns
      List<Column> pkColumns = new ArrayList<>();
      for(Field field : pkFields) {
         com.digitolio.jdbi.annotations.Column annotation = field.getAnnotation(com.digitolio.jdbi.annotations.Column.class);
         boolean nullable = annotation != null ? annotation.nullable() : com.digitolio.jdbi.annotations.Column.defaultNullable;
         pkColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName()), nullable));
      }

      com.digitolio.jdbi.annotations.Table tableAnnotation = type.getAnnotation(com.digitolio.jdbi.annotations.Table.class);
      String tableName = tableAnnotation != null ?
                            tableAnnotation.name() :
                            fieldTranslatingStrategy.translate(type.getSimpleName());

      return new Table(tableName, allColumns, pkColumns);
   }

   public List<Field> getInheritedFields(Class<?> type) {
      List<Field> fields = new ArrayList<>();
      for(Class<?> c = type; c != null; c = c.getSuperclass()) {
         fields.addAll(Arrays.asList(c.getDeclaredFields()));
      }
      return fields;
   }
}
