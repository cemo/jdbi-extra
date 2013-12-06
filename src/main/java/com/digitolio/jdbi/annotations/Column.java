package com.digitolio.jdbi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    boolean nullable() default defaultNullable;

    String defaultValue() default defaultValueConstant ;

    String[] unique() default {} ;

    String[] index() default {} ;

    int length() default defaultLength ;

    boolean ignore() default false;

    final static String defaultValueConstant = "$#!!#$";

    final static boolean defaultNullable = false;

    final static int defaultLength = -1;
}