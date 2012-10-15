package com.digitolio.jdbi;

import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;

import java.util.Map;

public class AutoSelectByPKGenerator implements SqlGenerator {

    private Table table;

    private String clause;


    public AutoSelectByPKGenerator(Table table) {
        this.table = table;
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }


    private String initAllClause() {
        return initUpdatePart() + initWherePart();
    }

    private String initUpdatePart() {
       return "SELECT * FROM ".concat(table.getTableDb());
    }

    private String initWherePart() {
        Map<String,String> primaryKeys = table.getPrimaryKeys();
        StringBuilder builder = new StringBuilder(" WHERE ");
        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            builder.append(entry.getValue()).append(" = :").append(entry.getKey()).append(" AND ");
        }
        return builder.substring(0,builder.length() - 5);
    }

    @Override
    public String generate(Binding params) {
        return clause;
    }
}
