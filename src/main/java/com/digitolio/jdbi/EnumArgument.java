package com.digitolio.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class EnumArgument implements Argument {

    private final Enum<?> value;

    public EnumArgument(Enum<?> value) {
        this.value = value;
    }

    @Override
    public void apply(int position,
                      PreparedStatement statement,
                      StatementContext ctx) throws SQLException {
        if (value != null) {
            statement.setObject(position, value.name());
        } else {
            statement.setNull(position, Types.OTHER);
        }
    }
}
