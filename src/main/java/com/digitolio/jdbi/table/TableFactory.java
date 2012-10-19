package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

final class TableFactory {

    Table newInstance(Connection con, TranslateTablePair translateTablePair) {
        TranslatingStrategyAware translater = translateTablePair.getTranslater();

        String tableDb = translater.getPropertyTranslatingStrategy()
            .translate(translateTablePair.getType().getSimpleName());

        Map<String, String> columns = generateInfo(fetchAllColumns(con, tableDb), translateTablePair);
        Map<String, String> primaryKeys = generateInfo(fetchPrimaryKeys(con, tableDb), translateTablePair);

        return new Table(tableDb, primaryKeys, columns);
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

    static Map<String, String> generateInfo(ResultSet rs, TranslateTablePair tablePair) {
        try {
            Map<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
            Class type = tablePair.getType();
            TranslatingStrategyAware translater = tablePair.getTranslater();
            BeanInfo info = Introspector.getBeanInfo(type);
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                properties.put(translater.getPropertyTranslatingStrategy().translate(descriptor.getName()), descriptor);
            }

            Map<String, String> biMapStorage = new HashMap<String, String>();

            while (rs.next()) {
                String columnLabel = rs.getString("COLUMN_NAME");
                PropertyDescriptor propertyDescriptor = properties.get(columnLabel);
                biMapStorage.put(propertyDescriptor.getDisplayName(), columnLabel);
            }

            return biMapStorage;
        } catch(SQLException e) {
            throw new IllegalArgumentException(e);
        } catch(IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
