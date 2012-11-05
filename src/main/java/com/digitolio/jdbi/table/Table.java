package com.digitolio.jdbi.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Table {

    private final String tableName;
    private final List<Column> primaryKeyColumns;
    private final List<Column> nonPrimaryKeyColumns;
    private final List<Column> allColumns;
    private final Map<String, Column> allColumnsMap;

    public Table(String tableName, List<Column> allColumns, List<Column> primaryKeyColumns) {
        this.tableName = tableName;
        this.primaryKeyColumns = primaryKeyColumns;
        this.allColumns = allColumns;
        this.allColumnsMap = transformToMap(allColumns);
        this.nonPrimaryKeyColumns = new ArrayList<Column>(allColumns);
        nonPrimaryKeyColumns.removeAll(primaryKeyColumns);
    }

    private Map<String, Column> transformToMap(List<Column> pkColumns) {
        HashMap<String, Column> map = new HashMap<String, Column>();
        for (Column pkColumn : pkColumns) {
            map.put(pkColumn.getDatabaseName(), pkColumn);
        }
        return map;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getAllColumns() {
        return allColumns;
    }

    public List<Column> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public List<Column> getNonPrimaryKeyColumns() {
        return nonPrimaryKeyColumns;
    }

    public Column getColumnByDatabaseColumnName(String databaseColumnName) {
        return allColumnsMap.get(databaseColumnName);
    }
}
