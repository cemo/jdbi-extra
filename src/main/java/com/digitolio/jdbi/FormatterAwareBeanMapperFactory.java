package com.digitolio.jdbi;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class FormatterAwareBeanMapperFactory implements ResultSetMapperFactory {

   NamingStrategy dbNamingStrategy;

   NamingStrategy fieldNamingStrategy;

   public FormatterAwareBeanMapperFactory(NamingStrategy dbNamingStrategy, NamingStrategy fieldNamingStrategy) {
      this.dbNamingStrategy = dbNamingStrategy;
      this.fieldNamingStrategy = fieldNamingStrategy;
   }

   @Override
   public boolean accepts(Class type, StatementContext ctx) {
      return true;
   }

   @SuppressWarnings("unchecked")
   @Override
   public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
      return new FormatterAwareMapper(type, dbNamingStrategy, fieldNamingStrategy);
   }
}
