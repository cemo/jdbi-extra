package com.digitolio.jdbi;

import com.digitolio.StrategyAwareDBI;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TranslationAwareBeanMapperFactory implements ResultSetMapperFactory {

    private TranslatingStrategyAware translater;

    public TranslationAwareBeanMapperFactory() {}

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        translater = (TranslatingStrategyAware) ctx.getAttribute(StrategyAwareDBI.TRANSLATING_STRATEGY);
        return new TranslationAwareMapper(type, translater);
    }
}
