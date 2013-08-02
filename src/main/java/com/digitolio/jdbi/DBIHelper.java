package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.ResultIterator;
import org.skife.jdbi.v2.tweak.HandleCallback;

import java.util.Map;

/**
 * @author Cemo
 */
public class DBIHelper {

   final static TranslationAwareBeanMapperFactory beanMapperFactory = new TranslationAwareBeanMapperFactory();

   final TranslatingStrategyAware strategy;

   public DBIHelper(TranslatingStrategyAware strategy) {
      this.strategy = strategy;
   }

   public <T> T select(DBI dbi,
                       final Class<T> type,
                       String sql,
                       final Object... args) {

      String translatedType = translateType(type);
      final String typeReplacedSql = sql.replace("<type>", translatedType);

      T t = dbi.withHandle(new HandleCallback<T>() {
         public T withHandle(Handle h) throws Exception {
            Query<Map<String, Object>> map = h.createQuery(typeReplacedSql);
            for(int i = 0; i < args.length; i++) {
               map.bind(i, args[i]);
            }
            map.registerMapper(beanMapperFactory);
            Query<T> ts = map.mapTo(type);
            ResultIterator<T> iterator = ts.iterator();
            if(iterator.hasNext()) {
               return iterator.next();
            } else {
               return null;
            }
         }
      });

      return t;
   }

   private <T> String translateType(Class<T> type) {
      return strategy.getPropertyTranslatingStrategy().translate(type.getSimpleName());

   }
}
