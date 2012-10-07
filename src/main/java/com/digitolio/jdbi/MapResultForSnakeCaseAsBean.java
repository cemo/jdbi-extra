package com.digitolio.jdbi;

import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizer;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizerFactory;
import org.skife.jdbi.v2.sqlobject.SqlStatementCustomizingAnnotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.sql.SQLException;

@Retention(RetentionPolicy.RUNTIME)
@SqlStatementCustomizingAnnotation(MapResultForSnakeCaseAsBean.MapAsBeanFactory.class)
@Target(ElementType.METHOD)
public @interface MapResultForSnakeCaseAsBean
{
    NamingStrategy dbNamingStrategy() default NamingStrategy.UPPER_UNDERSCORE;

    NamingStrategy fieldNamingStrategy() default NamingStrategy.IDENTICAL;

    public static class MapAsBeanFactory implements SqlStatementCustomizerFactory
    {

        @Override
        public SqlStatementCustomizer createForMethod(final Annotation annotation, Class sqlObjectType, Method method)
        {
            return new SqlStatementCustomizer()
            {
                @Override
                public void apply(SQLStatement s) throws SQLException
                {
                   Query q = (Query) s;
                   NamingStrategy dbNamingStrategy = ((MapResultForSnakeCaseAsBean) annotation).dbNamingStrategy();
                   NamingStrategy fieldNamingStrategy = ((MapResultForSnakeCaseAsBean) annotation).fieldNamingStrategy();
                   q.registerMapper(new FormatterAwareBeanMapperFactory(dbNamingStrategy,fieldNamingStrategy));
                }
            };
        }

        @Override
        public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType)
        {
            throw new UnsupportedOperationException("Not allowed on type");
        }

        @Override
        public SqlStatementCustomizer createForParameter(Annotation annotation, Class sqlObjectType, Method method, Object arg)
        {
            throw new UnsupportedOperationException("Not allowed on parameter");
        }
    }
}
