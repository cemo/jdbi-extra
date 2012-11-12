package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.table.Column;
import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.tweak.Argument;

import java.util.List;

public class SqlInsert extends SqlSupport {

    private String clause;

    public SqlInsert(Table table) {
        super(table);
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }

    private String initAllClause() {
        return initUpdatePart();
    }

    private String initUpdatePart() {
        return "INSERT INTO ".concat(getTableName());
    }

    @Override
    public String generate(Binding params) {
        List<Column> allColumns = table.getAllColumns();
        StringBuilder builder = new StringBuilder(" SET ");

        for (Column entry : allColumns) {
            String input = entry.getFieldName();
            Argument argument = params.forName(input);
            if (argument != null && !"null".equals(argument.toString())) {
                builder.append(entry.getDatabaseName()).append(" = :").append(input).append(" , ");
            }
        }
        String set = builder.substring(0, builder.length() - 2);
        return clause.concat(set);
    }
}
