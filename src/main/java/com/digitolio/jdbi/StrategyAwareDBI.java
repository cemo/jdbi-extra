package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.SnakeCaseTranslatingStrategy;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.DBI;

public class StrategyAwareDBI{

    public static final String TRANSLATING_STRATEGY = "TRANSLATING_STRATEGY";

    public static DBI enhanceDBIForSnakeCase(DBI dbi){
        return enhanceDBI(dbi, new SnakeCaseTranslatingStrategy());
    }

    public static DBI enhanceDBI(DBI dbi, TranslatingStrategyAware translatingStrategy){
        dbi.registerArgumentFactory(new EnumArgumentFactory());
        dbi.define(StrategyAwareDBI.TRANSLATING_STRATEGY, translatingStrategy);
        return dbi;
    }
}
