package com.digitolio.jdbi.table;

import java.util.HashMap;
import java.util.Map;

public final class Table {

    private final String tableCode;
    private final String tableDb;
    private final Map<String, String> primaryKeys;
    private final Map<String, String> nonPrimaryKeys;
    private final Map<String, String> columns;

    public Table(String tableCode, String tableDb, Map<String, String> primaryKeys, Map<String, String> columns) {
        this.tableCode = tableCode;
        this.tableDb = tableDb;
        this.primaryKeys = primaryKeys;
        this.columns = columns;
        nonPrimaryKeys = diffMaps(columns, primaryKeys);
    }

    private Map<String, String> diffMaps(Map<String, String> columns, Map<String, String> primaryKeys) {
        HashMap<String, String> map= new HashMap<String, String>(columns);
        for (String s : primaryKeys.keySet()) {
            map.remove(s);
        }
        return map;
    }

    public String getTableCode() {
        return tableCode;
    }

    public String getTableDb() {
        return tableDb;
    }

    public Map<String, String> getPrimaryKeys() {
        return primaryKeys;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public Map<String, String> getNonPrimaryKeys() {
        return nonPrimaryKeys;
    }
}
