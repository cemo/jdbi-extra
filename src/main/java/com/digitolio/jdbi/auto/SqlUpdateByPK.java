package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.table.Column;
import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.tweak.Argument;

import java.util.List;

public class SqlUpdateByPK extends SqlSupport {

    private String clause;

    public SqlUpdateByPK(Table table) {
        super(table);
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }

    private String initAllClause() {
        return initUpdatePart() + " %s " + initWherePart();
    }

    private String initUpdatePart() {
       return "UPDATE ".concat(table.getTableName());
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
        List<Column> nonPrimaryKeyColumns = table.getNonPrimaryKeyColumns();
        StringBuilder builder = new StringBuilder("SET ");

        for (Column entry : nonPrimaryKeyColumns) {
            String input = entry.getFieldName();
            Argument argument = params.forName(input);


//            if(!entry.isNullable() ||  (argument !=null && !"null".equals(argument.toString()))){
                builder.append(entry.getDatabaseName()).append(" = :").append(input).append(" , ");
//            }
        }
        String set = builder.substring(0, builder.length() - 2);
        return String.format(clause,set);
    }
}
