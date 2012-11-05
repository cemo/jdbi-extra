package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.SqlGenerator;
import com.digitolio.jdbi.table.Column;
import com.digitolio.jdbi.table.Table;

import java.util.List;

/**
 * @author C.Koc
 */
public abstract class SqlSupport implements SqlGenerator {

    Table table;

    SqlSupport(Table table) {
        this.table = table;
    }

    String getWherePart() {
        List<Column> primaryKeys = table.getPrimaryKeyColumns();
        StringBuilder builder = new StringBuilder(" WHERE ");
        for (Column entry : primaryKeys) {
            builder.append(entry.getDatabaseName()).append(" = :").append(entry.getFieldName()).append(" AND ");
        }
        return builder.substring(0,builder.length() - 5);
    }


    String getTableName() {
        return table.getTableName();
    }

}
