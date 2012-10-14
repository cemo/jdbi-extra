package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.SnakeCaseTranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
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
public @interface MapResultForSnakeCaseAsBean {

    Class<? extends TranslatingStrategyAware> translaterClass() default SnakeCaseTranslatingStrategy.class;

    public static class MapAsBeanFactory implements SqlStatementCustomizerFactory {

        @Override
        public SqlStatementCustomizer createForMethod(final Annotation annotation, Class sqlObjectType, Method method) {
            return new SqlStatementCustomizer() {
                @Override
                public void apply(SQLStatement s) throws SQLException {
                    Query q = (Query) s;
                    Class<? extends TranslatingStrategyAware> translater = ((MapResultForSnakeCaseAsBean) annotation).translaterClass();
                    q.registerMapper(new TranslationAwareBeanMapperFactory(translater));
                }
            };
        }

        @Override
        public SqlStatementCustomizer createForType(Annotation annotation, Class sqlObjectType) {
            throw new UnsupportedOperationException("Not allowed on type");
        }

        @Override
        public SqlStatementCustomizer createForParameter(Annotation annotation, Class sqlObjectType, Method method, Object arg) {
            throw new UnsupportedOperationException("Not allowed on parameter");
        }
    }
}
