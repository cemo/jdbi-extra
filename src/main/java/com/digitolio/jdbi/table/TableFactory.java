package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

final class TableFactory {

    Table newInstance(Connection con, TranslateTableBean translateTableBean) {
        TranslatingStrategyAware translater = translateTableBean.getTranslater();
        String tableDb = translater.getPropertyTranslatingStrategy().translate(translateTableBean.getType().getSimpleName());
        String tableCode = translater.getDbTranslatingStrategy().translate(tableDb);

        return new Table(
            tableCode,
            tableDb,
            generateInfo(fetchPrimaryKeys(con, tableDb), translater),
            generateInfo(fetchAllColumns(con, tableDb), translater));
    }

    private ResultSet fetchAllColumns(Connection con, String table) {
        try {
            DatabaseMetaData md = con.getMetaData();
            return md.getColumns(null, null, table, null);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet fetchPrimaryKeys(Connection con, String table) {
        try {
            DatabaseMetaData md = con.getMetaData();
            return md.getPrimaryKeys(null, null, table);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Map<String, String> generateInfo(ResultSet rs, TranslatingStrategyAware translater) {
        try {
            Map<String, String> biMapStorage = new HashMap<String, String>();
            while (rs.next()) {
                String columnLabel = rs.getString("COLUMN_NAME");
                biMapStorage.put(translater.getDbTranslatingStrategy().translate(columnLabel), columnLabel);
            }
            return biMapStorage;
        } catch(SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
