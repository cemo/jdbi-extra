package com.digitolio.jdbi.table;

import com.digitolio.jdbi.annotations.PK;
import com.digitolio.jdbi.strategy.TranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author C.Koc
 */
public class TableResolver {

   public Table resolve(Class<?> type, TranslatingStrategyAware strategy) {

      List<Field> list = getInheritedFields(type);
      TranslatingStrategy fieldTranslatingStrategy = strategy.getPropertyTranslatingStrategy();

      // columns
      List<Column> allColumns = getColumns(list, fieldTranslatingStrategy);

      List<Column> pkColumns = getPkColumns(list, fieldTranslatingStrategy);

      Map<String, List<Column>> uniqueIndexes = getUniqueIndexes(allColumns);
      Map<String, List<Column>> indexes = getIndexes(allColumns);

      com.digitolio.jdbi.annotations.Table tableAnnotation = type.getAnnotation(com.digitolio.jdbi.annotations.Table.class);
      String tableName = tableAnnotation != null ?
                            tableAnnotation.name() :
                            fieldTranslatingStrategy.translate(type.getSimpleName());

      return new Table(tableName, allColumns, pkColumns, uniqueIndexes, indexes);
   }

   private Map<String, List<Column>> getUniqueIndexes(List<Column> allColumns) {
      Map<String, List<Column>> map = new HashMap<>();
      for(Column column : allColumns) {

         String[] unique = column.getUnique();
         for(String i : unique) {
            List<Column> columns = map.get(i);
            if(columns == null){
               columns = new ArrayList<>();
               map.put(i, columns);
            }
            columns.add(column);
         }
      }
      return map;
   }
   private Map<String, List<Column>> getIndexes(List<Column> allColumns) {
      Map<String, List<Column>> map = new HashMap<>();
      for(Column column : allColumns) {

         String[] indexes = column.getIndex();
         for(String i : indexes) {
            List<Column> columns = map.get(i);
            if(columns == null){
               columns = new ArrayList<>();
               map.put(i, columns);
            }
            columns.add(column);
         }
      }
      return map;
   }

   private List<Column> getColumns(List<Field> list, TranslatingStrategy fieldTranslatingStrategy) {
      List<Column> allColumns = new ArrayList<>();
      for(Field field : list) {
         com.digitolio.jdbi.annotations.Column annotation = field.getAnnotation(com.digitolio.jdbi.annotations.Column.class);
         if(annotation != null && annotation.ignore()){
            continue;
         }
         boolean nullable = annotation != null ? annotation.nullable() : com.digitolio.jdbi.annotations.Column.defaultNullable;
         String[] unique = annotation == null || annotation.unique().length == 0  ? new String[]{} : annotation.unique();
         String[] index = annotation == null || annotation.index().length == 0  ? new String[]{} : annotation.index();
         Integer length = annotation == null ? com.digitolio.jdbi.annotations.Column.defaultLength : annotation.length();
         allColumns.add(new Column(field, fieldTranslatingStrategy.translate(field.getName()), nullable, unique, index, length));
      }
      return allColumns;
   }

   private List<Column> getPkColumns(List<Field> list, TranslatingStrategy fieldTranslatingStrategy) {// pk
      List<Field> pkFields = new ArrayList<>();
      for(Field field : list) {
         if(field.getAnnotation(PK.class) != null) {
            pkFields.add(field);
         }
      }

      // pk fields
      List<Column> pkColumns = getColumns(pkFields, fieldTranslatingStrategy);
      return pkColumns;
   }

   public List<Field> getInheritedFields(Class<?> type) {
      List<Field> fields = new ArrayList<>();
      for(Class<?> c = type; c != null; c = c.getSuperclass()) {
         fields.addAll(Arrays.asList(c.getDeclaredFields()));
      }
      return fields;
   }
}
