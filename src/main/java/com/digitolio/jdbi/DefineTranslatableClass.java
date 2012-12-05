package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.TranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizer;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizerFactory;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizingAnnotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;


@SqlStatementCustomizingAnnotation(DefineTranslatableClass.Factory.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefineTranslatableClass {
   /**
    * The key for the attribute to set. The value will be the value passed to the annotated argument
    */
   String value();

   static class Factory implements SqlStatementCustomizerFactory {
      public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType) {
         throw new UnsupportedOperationException("Not allowed on Type");
      }

      public SqlStatementCustomizer createForMethod(Annotation annotation, Class sqlObjectType, Method method) {
         throw new UnsupportedOperationException("Not allowed on Method");
      }

      public SqlStatementCustomizer createForParameter(Annotation annotation, Class sqlObjectType, Method method, final Object arg) {
         DefineTranslatableClass d = (DefineTranslatableClass) annotation;
         final String key = d.value();
         final Class<?> clazz = (Class<?>) arg;
         return new SqlStatementCustomizer() {
            public void apply(SQLStatement q) {
               TranslatingStrategy translatable = getTranslatable(q.getContext());
               q.define(key, translatable.translate(clazz.getSimpleName()));
            }
         };
      }

      private TranslatingStrategy getTranslatable(StatementContext ctx) {
         TranslatingStrategyAware attribute = (TranslatingStrategyAware) ctx.getAttribute(StrategyAwareDBI.TRANSLATING_STRATEGY);
         return attribute.getPropertyTranslatingStrategy();
      }
   }
}