package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.util.concurrent.ConcurrentHashMap;

public class TableRegistry {

    private final static TableRegistry SINGLETON = new TableRegistry();

    private final TableResolver tableFactory = new TableResolver();

    private final ConcurrentHashMap<TranslateTablePair, Table> tables = new ConcurrentHashMap<TranslateTablePair, Table>();

    private TableRegistry() { }

    public static TableRegistry getInstance() {
        return SINGLETON;
    }

    public Table getTable(Class type, TranslatingStrategyAware translater) {
        TranslateTablePair key = new TranslateTablePair(type, translater);
        return getTable(key);
    }

    public Table getTable( TranslateTablePair key) {
        if (tables.containsKey(key)) {
            return tables.get(key);
        } else {
            Table table = tableFactory.resolve(key.getType(), key.getTranslater());
            tables.put(key, table);
            return table;
        }
    }
}