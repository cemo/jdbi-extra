package com.digitolio.jdbi.table;

import java.lang.reflect.Field;

/**
 * @author C.Koc
 */
public class Column {

   Field field;

   String databaseName;

   private boolean nullable;

   private String[] unique;

   public Column(Field field, String databaseName, boolean nullable, String[] unique) {
      this.field = field;
      this.databaseName = databaseName;
      this.nullable = nullable;
      this.unique = unique;
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

   public boolean isNullable() {
      return nullable;
   }

   public void setNullable(boolean nullable) {
      this.nullable = nullable;
   }

   public String[] getUnique() {
      return unique;
   }

   public void setUnique(String[] unique) {
      this.unique = unique;
   }
}
