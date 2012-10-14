package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.SnakeCaseTranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizer;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizerFactory;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizingAnnotation;
import org.skife.jdbi.v2.tweak.StatementRewriter;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@SqlStatementCustomizingAnnotation(AutoUpdateByPK.Factory.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AutoUpdateByPK {

    Class<? extends TranslatingStrategyAware> translaterClass() default SnakeCaseTranslatingStrategy.class;

    public static class Factory implements SqlStatementCustomizerFactory {

       public SqlStatementCustomizer createForMethod(Annotation annotation, Class sqlObjectType, Method method) {

          AutoUpdateByPK anno = (AutoUpdateByPK) annotation;
          Class<?> beanType = findBeanType(method);
          try {
             final StatementRewriter rw = new AutoUpdateByPKWriter(anno.translaterClass(), beanType);
             return new SqlStatementCustomizer() {
                public void apply(SQLStatement q) {
                   q.setStatementRewriter(rw);
                }
             };
          } catch(Exception e) {
             throw new IllegalStateException(e);
          }
       }

       private Class<?> findBeanType(Method method) {
          Annotation[][] parameterAnnotationAll = method.getParameterAnnotations();
          for(int i = 0; i < parameterAnnotationAll.length; i++) {
             Annotation[] parameterAnnotations = parameterAnnotationAll[i];
             for(Annotation parameterAnnotation : parameterAnnotations) {
                if(parameterAnnotation.annotationType() == BindBean.class) {
                   return method.getParameterTypes()[i];
                }
             }
          }
          throw new IllegalArgumentException("Method does not has a BindBean annotation");
       }

       public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType) {
          throw new IllegalStateException("Not defined on parameters!");
       }

       public SqlStatementCustomizer createForParameter(Annotation annotation, Class sqlObjectType, Method method, Object arg) {
          throw new IllegalStateException("Not defined on parameters!");
       }
    }
}
