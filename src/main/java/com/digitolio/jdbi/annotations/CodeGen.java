package com.digitolio.jdbi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface CodeGen {

    public final static String CODE_GEN = "code-gen";
    public final static String DDL_GEN = "ddl-gen";
    public final static String ALL = "all";
    public final static String NONE = "none";

    String[] ignore() default {NONE};
}