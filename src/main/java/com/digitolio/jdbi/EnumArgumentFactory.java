package com.digitolio.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class EnumArgumentFactory implements ArgumentFactory<Enum<?>> {

    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
        return value instanceof Enum;
    }

    @Override
    public Argument build(Class<?> expectedType, Enum<?> value, StatementContext ctx) {
        return new EnumArgument(value);
    }
}
