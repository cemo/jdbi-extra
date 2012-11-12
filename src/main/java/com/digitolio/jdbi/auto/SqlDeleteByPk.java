package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.table.Table;
import org.skife.jdbi.v2.Binding;

public class SqlDeleteByPk extends SqlSupport{

    private String clause;

    public SqlDeleteByPk(Table table) {
        super(table);
        initConstantSqls();
    }

    private void initConstantSqls() {
        clause = initAllClause();
    }

    private String initAllClause() {
        return initUpdatePart() + getWherePart();
    }

    private String initUpdatePart() {
       return "DELETE FROM ".concat(table.getTableName());
    }

    @Override
    public String generate(Binding params) {
        return clause;
    }
}
