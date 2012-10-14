package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

public class TableRegistry {

    private final static TableRegistry SINGLETON = new TableRegistry();

    private final TableFactory tableFactory = new TableFactory();

    private final ConcurrentHashMap<TranslateTableBean, Table> tables = new ConcurrentHashMap<TranslateTableBean, Table>();

    private TableRegistry() { }

    public static TableRegistry getInstance() {
        return SINGLETON;
    }

    public Table getTable(Connection con, Class type, TranslatingStrategyAware translater) {
        TranslateTableBean key = new TranslateTableBean(type, translater);
        return getTable(con, key);
    }

    public Table getTable(Connection con, TranslateTableBean key) {
        if (tables.containsKey(key)) {
            return tables.get(key);
        } else {
            Table table = tableFactory.newInstance(con, key);
            tables.put(key, table);
            return table;
        }
    }
}