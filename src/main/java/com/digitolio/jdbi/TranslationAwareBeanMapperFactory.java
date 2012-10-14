package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TranslationAwareBeanMapperFactory implements ResultSetMapperFactory {

    private Class<? extends TranslatingStrategyAware> translaterClass;

    public TranslationAwareBeanMapperFactory(Class<? extends TranslatingStrategyAware> translaterClass) {
        this.translaterClass = translaterClass;
    }

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        return new TranslationAwareMapper(type, translaterClass);
    }
}
