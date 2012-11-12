package com.digitolio.jdbi.annotations;

import com.digitolio.jdbi.auto.AutoSelectByPKWriter;
import com.digitolio.jdbi.support.Resolver;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizer;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizerFactory;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizingAnnotation;
import org.skife.jdbi.v2.tweak.StatementRewriter;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@SqlStatementCustomizingAnnotation(AutoSelectByPK.Factory.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AutoSelectByPK {

    public static class Factory implements SqlStatementCustomizerFactory {

       public SqlStatementCustomizer createForMethod(Annotation annotation, Class sqlObjectType, Method method) {

          Class<?> beanType = Resolver.findBeanType(sqlObjectType, method);
          try {
             final StatementRewriter rw = new AutoSelectByPKWriter(beanType);
             return new SqlStatementCustomizer() {
                public void apply(SQLStatement q) {
                   q.setStatementRewriter(rw);
                }
             };
          } catch(Exception e) {
             throw new IllegalStateException(e);
          }
       }

       public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType) {
          throw new IllegalStateException("Not defined on parameters!");
       }

       public SqlStatementCustomizer createForParameter(Annotation annotation, Class sqlObjectType, Method method, Object arg) {
          throw new IllegalStateException("Not defined on parameters!");
       }
    }
}
