package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.table.Column;
import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;

import java.util.List;

public class SqlSelectByPK extends SqlSupport{

    private String clause;

    public SqlSelectByPK(Table table) {
        super(table);
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }

    private String initAllClause() {
        return initUpdatePart() + initWherePart();
    }

    private String initUpdatePart() {
       return "SELECT * FROM ".concat(table.getTableName());
    }

    private String initWherePart() {
        List<Column> primaryKeys = table.getPrimaryKeyColumns();
        StringBuilder builder = new StringBuilder(" WHERE ");
        for (Column entry : primaryKeys) {
            builder.append(entry.getDatabaseName()).append(" = :").append(entry.getFieldName()).append(" AND ");
        }
        return builder.substring(0,builder.length() - 5);
    }

    @Override
    public String generate(Binding params) {
        return clause;
    }
}
