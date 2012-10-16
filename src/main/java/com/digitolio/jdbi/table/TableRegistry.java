package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

public class TableRegistry {

    private final static TableRegistry SINGLETON = new TableRegistry();

    private final TableFactory tableFactory = new TableFactory();

    private final ConcurrentHashMap<TranslateTablePair, Table> tables = new ConcurrentHashMap<TranslateTablePair, Table>();

    private TableRegistry() { }

    public static TableRegistry getInstance() {
        return SINGLETON;
    }

    public Table getTable(Connection con, Class type, TranslatingStrategyAware translater) {
        TranslateTablePair key = new TranslateTablePair(type, translater);
        return getTable(con, key);
    }

    public Table getTable(Connection con, TranslateTablePair key) {
        if (tables.containsKey(key)) {
            return tables.get(key);
        } else {
            Table table = tableFactory.newInstance(con, key);
            tables.put(key, table);
            return table;
        }
    }
}