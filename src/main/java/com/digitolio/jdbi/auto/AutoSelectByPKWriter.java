package com.digitolio.jdbi.auto;

import com.digitolio.jdbi.StrategyAwareDBI;
import com.digitolio.jdbi.strategy.TranslatingStrategyAware;
import com.digitolio.jdbi.table.Table;
import com.digitolio.jdbi.table.TableRegistry;
import com.digitolio.jdbi.table.TranslateTablePair;
import org.skife.jdbi.org.antlr.runtime.ANTLRStringStream;
import org.skife.jdbi.org.antlr.runtime.Token;
import org.skife.jdbi.rewriter.colon.ColonStatementLexer;
import org.skife.jdbi.v2.Binding;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.RewrittenStatement;
import org.skife.jdbi.v2.tweak.StatementRewriter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.skife.jdbi.rewriter.colon.ColonStatementLexer.*;

public class AutoSelectByPKWriter implements StatementRewriter {

    private Boolean initialized = false;

    private SqlSelectByPK sqlGenerator;

    private Class<?> type;

    public AutoSelectByPKWriter(Class<?> type) {this.type = type;}

    public RewrittenStatement rewrite(String sql, Binding params, StatementContext ctx) {
       Argument argument = params.forPosition(0);

//       SqlFactory.insert()

        if (!initialized) {
            TranslatingStrategyAware translatingStrategyAware = (TranslatingStrategyAware) ctx.getAttribute(
                    StrategyAwareDBI.TRANSLATING_STRATEGY);
            Table table = TableRegistry.getInstance().getTable(new TranslateTablePair(type,translatingStrategyAware));
            sqlGenerator = new SqlSelectByPK(table);
            initialized = true;
        }
        final ParsedStatement stmt = new ParsedStatement();
        try {
            String newSql = parseString(sqlGenerator.generate(params), stmt);
            return new MyRewrittenStatement(newSql, stmt, ctx);
        } catch(IllegalArgumentException e) {
            throw new UnableToCreateStatementException("Exception parsing for named parameter replacement", e, ctx);
        }

    }

    String parseString(final String sql, final ParsedStatement stmt) throws IllegalArgumentException {
        StringBuilder b = new StringBuilder();
        ColonStatementLexer lexer = new ColonStatementLexer(new ANTLRStringStream(sql));
        Token t = lexer.nextToken();
        while (t.getType() != ColonStatementLexer.EOF) {
            switch(t.getType()) {
                case LITERAL:
                    b.append(t.getText());
                    break;
                case NAMED_PARAM:
                    stmt.addNamedParamAt(t.getText().substring(1, t.getText().length()));
                    b.append("?");
                    break;
                case QUOTED_TEXT:
                    b.append(t.getText());
                    break;
                case DOUBLE_QUOTED_TEXT:
                    b.append(t.getText());
                    break;
                case POSITIONAL_PARAM:
                    b.append("?");
                    stmt.addPositionalParamAt();
                    break;
                case ESCAPED_TEXT:
                    b.append(t.getText().substring(1));
                    break;
                default:
                    throw new IllegalArgumentException("Not a valid expression");
            }
            t = lexer.nextToken();
        }
        return b.toString();
    }


    private static class MyRewrittenStatement implements RewrittenStatement {
        private final String sql;
        private final ParsedStatement stmt;
        private final StatementContext context;

        public MyRewrittenStatement(String sql, ParsedStatement stmt, StatementContext ctx) {
            this.context = ctx;
            this.sql = sql;
            this.stmt = stmt;
        }

        public void bind(Binding params, PreparedStatement statement) throws SQLException {
            if (stmt.positionalOnly) {
                // no named params, is easy
                boolean finished = false;
                for (int i = 0; !finished; ++i) {
                    final Argument a = params.forPosition(i);
                    if (a != null) {
                        try {
                            a.apply(i + 1, statement, this.context);
                        } catch(SQLException e) {
                            throw new UnableToExecuteStatementException(String.format("Exception while binding positional param at (0 based) position %d", i), e, context);
                        }
                    } else {
                        finished = true;
                    }
                }
            } else {
                //List<String> named_params = stmt.params;
                int i = 0;
                for (String named_param : stmt.params) {
                    if ("*".equals(named_param)) {
                        continue;
                    }
                    Argument a = params.forName(named_param);
                    if (a == null) {
                        a = params.forPosition(i);
                    }

                    if (a == null) {
                        String msg = String.format("Unable to execute, no named parameter matches " +
                                                       "\"%s\" and no positional param for place %d (which is %d in " +
                                                       "the JDBC 'start at 1' scheme) has been set.", named_param, i, i + 1);
                        throw new UnableToExecuteStatementException(msg, context);
                    }

                    try {
                        a.apply(i + 1, statement, this.context);
                    } catch(SQLException e) {
                        throw new UnableToCreateStatementException(String.format("Exception while binding '%s'", named_param), e, context);
                    }
                    i++;
                }
            }
        }

        public String getSql() {
            return sql;
        }
    }

    static class ParsedStatement {
        private boolean positionalOnly = true;
        private List<String> params = new ArrayList<String>();

        public void addNamedParamAt(String name) {
            positionalOnly = false;
            params.add(name);
        }

        public void addPositionalParamAt() {
            params.add("*");
        }
    }
}
