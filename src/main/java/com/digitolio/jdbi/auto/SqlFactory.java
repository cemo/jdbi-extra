package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import com.digitolio.jdbi.table.Table;
import com.digitolio.jdbi.table.TableRegistry;
import com.digitolio.jdbi.table.TranslateTablePair;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.tweak.Argument;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cemo
 */
public class SqlFactory {

   private static ConcurrentHashMap<Class<?>, SqlInsert> inserts = new ConcurrentHashMap<>();
   private static ConcurrentHashMap<Class<?>, SqlUpdateByPK> updates = new ConcurrentHashMap<>();
   private static ConcurrentHashMap<Class<?>, SqlPartialUpdateByPK> partialUpdates = new ConcurrentHashMap<>();

   public static SqlInsert insert(Class<?> type, TranslatingStrategyAware translatingStrategyAware) {

      SqlInsert support = inserts.get(type);

      if(support == null) {
         TranslateTablePair key = new TranslateTablePair(type, translatingStrategyAware);
         Table table = TableRegistry.getInstance().getTable(key);
         support = new SqlInsert(table);
         inserts.put(type, support);
      }

      return support;
   }

   public static SqlUpdateByPK update(Class<?> type, TranslatingStrategyAware translatingStrategyAware) {

      SqlUpdateByPK support = updates.get(type);

      if(support == null) {
         TranslateTablePair key = new TranslateTablePair(type, translatingStrategyAware);
         Table table = TableRegistry.getInstance().getTable(key);
         support = new SqlUpdateByPK(table);
         updates.put(type, support);
      }

      return support;
   }

   public static SqlPartialUpdateByPK partialUpdate(Class<?> type, TranslatingStrategyAware translatingStrategyAware) {

      SqlPartialUpdateByPK support = partialUpdates.get(type);

      if(support == null) {
         TranslateTablePair key = new TranslateTablePair(type, translatingStrategyAware);
         Table table = TableRegistry.getInstance().getTable(key);
         support = new SqlPartialUpdateByPK(table);
         partialUpdates.put(type, support);
      }

      return support;
   }


   public static Class<?> getClass(Binding params){
      Argument argument = params.forName("class");

      if(argument == null){
         throw new IllegalArgumentException("Binding param must have value class argument.");
      }
      try {
         Field f  = argument.getClass().getDeclaredField("value");
         f.setAccessible(true);
         return (Class<?>) f.get(argument);
      } catch(NoSuchFieldException | IllegalAccessException e) {
         throw new IllegalArgumentException("ObjectArgument must have value field.", e);
      }
   }
}
