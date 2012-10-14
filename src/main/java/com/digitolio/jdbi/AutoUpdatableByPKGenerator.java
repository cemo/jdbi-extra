package com.digitolio.jdbi;

import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.tweak.Argument;

import java.util.Map;

public class AutoUpdatableByPKGenerator implements SqlGenerator {

    private Table table;

    private String clause;


    public AutoUpdatableByPKGenerator(Table table) {
        this.table = table;
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }


    private String initAllClause() {
        return initUpdatePart() + " %s " + initWherePart();
    }

    private String initUpdatePart() {
       return "UPDATE ".concat(table.getTableDb());
    }

    private String initWherePart() {
        Map<String,String> primaryKeys = table.getPrimaryKeys();
        StringBuilder builder = new StringBuilder("WHERE ");
        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            builder.append(entry.getValue()).append(" = :").append(entry.getKey()).append(" AND ");
        }
        return builder.substring(0,builder.length() - 5);
    }

    @Override
    public String generate(Binding params) {
        Map<String, String> primaryKeys = table.getNonPrimaryKeys();
        StringBuilder builder = new StringBuilder("SET ");

        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            String input = entry.getKey();
            Argument argument = params.forName(input);
            if(argument !=null && !"null".equals(argument.toString())){
                builder.append(entry.getValue()).append(" = :").append(input).append(" , ");
            }
        }
        String set = builder.substring(0, builder.length() - 2);
        return String.format(clause,set);
    }
}
