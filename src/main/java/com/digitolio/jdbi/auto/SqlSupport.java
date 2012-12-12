package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.SqlGenerator;
import com.digitolio.jdbi.table.Column;
import com.digitolio.jdbi.table.Table;

import java.util.List;

/**
 * @author C.Koc
 */
public abstract class SqlSupport implements SqlGenerator {

    protected Table table;

    public SqlSupport(Table table) {
        this.table = table;
    }

    public String getWherePart() {
        List<Column> primaryKeys = table.getPrimaryKeyColumns();
        StringBuilder builder = new StringBuilder(" WHERE ");
        for (Column entry : primaryKeys) {
            builder.append(entry.getDatabaseName()).append(" = :").append(entry.getFieldName()).append(" AND ");
        }
        return builder.substring(0,builder.length() - 5);
    }


    public String getTableName() {
        return table.getTableName();
    }

}
