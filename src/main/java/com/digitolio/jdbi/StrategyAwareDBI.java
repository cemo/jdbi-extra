package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.SnakeCaseTranslatingStrategy;
import org.skife.jdbi.v2.DBI;

public class StrategyAwareDBI{

    public static final String TRANSLATING_STRATEGY = "TRANSLATING_STRATEGY";

    public static DBI enhanceDBI(DBI dbi){
        dbi.registerArgumentFactory(new EnumArgumentFactory());
        dbi.define(StrategyAwareDBI.TRANSLATING_STRATEGY, new SnakeCaseTranslatingStrategy());
        return dbi;
    }
}
