package com.digitolio.jdbi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    boolean nullable() default false;

    String defaultValue() default defaultValueConstant ;

    final static String defaultValueConstant = "$#!!#$";
}