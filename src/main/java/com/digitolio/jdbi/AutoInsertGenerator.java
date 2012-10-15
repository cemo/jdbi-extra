package com.digitolio.jdbi;

import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.tweak.Argument;

import java.util.Map;

public class AutoInsertGenerator implements SqlGenerator {

    private Table table;

    private String clause;


    public AutoInsertGenerator(Table table) {
        this.table = table;
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }


    private String initAllClause() {
        return initUpdatePart();
    }

    private String initUpdatePart() {
        return "INSERT INTO ".concat(table.getTableDb());
    }

    @Override
    public String generate(Binding params) {
        Map<String, String> primaryKeys = table.getColumns();
        StringBuilder builder = new StringBuilder(" SET ");

        for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
            String input = entry.getKey();
            Argument argument = params.forName(input);
            if (argument != null && !"null".equals(argument.toString())) {
                builder.append(entry.getValue()).append(" = :").append(input).append(" , ");
            }
        }
        String set = builder.substring(0, builder.length() - 2);
        return clause.concat(set);
    }
}
