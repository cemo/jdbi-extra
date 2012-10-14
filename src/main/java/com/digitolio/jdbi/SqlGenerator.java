package com.digitolio.jdbi;

import org.skife.jdbi.v2.Binding;

public interface SqlGenerator {
    public String generate(Binding params);
}
