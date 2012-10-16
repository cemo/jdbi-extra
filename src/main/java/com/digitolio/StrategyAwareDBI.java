package com.digitolio;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.tweak.ConnectionFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class StrategyAwareDBI extends DBI {

    public static final String TRANSLATING_STRATEGY = "TRANSLATING_STRATEGY";

    TranslatingStrategyAware translatingStrategyAware;

    public StrategyAwareDBI(DataSource dataSource, TranslatingStrategyAware translater) {
        super(dataSource);
        define(TRANSLATING_STRATEGY,translater);
    }

    public StrategyAwareDBI(ConnectionFactory connectionFactory, TranslatingStrategyAware translater) {
        super(connectionFactory);
        define(TRANSLATING_STRATEGY,translater);
    }

    public StrategyAwareDBI(String url, TranslatingStrategyAware translater) {
        super(url);
        define(TRANSLATING_STRATEGY,translater);
    }

    public StrategyAwareDBI(String url, Properties props, TranslatingStrategyAware translater) {
        super(url, props);
        define(TRANSLATING_STRATEGY,translater);
    }

    public StrategyAwareDBI(String url, String username, String password, TranslatingStrategyAware translater) {
        super(url, username, password);
        define(TRANSLATING_STRATEGY,translater);
    }
}
