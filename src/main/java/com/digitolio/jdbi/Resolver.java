package com.digitolio.jdbi;

import org.skife.jdbi.com.fasterxml.classmate.ResolvedType;
import org.skife.jdbi.com.fasterxml.classmate.TypeResolver;
import org.skife.jdbi.v2.sqlobject.BindBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

class Resolver {

    private final static TypeResolver tr = new TypeResolver();

    static Class<?> findBeanType(Class sqlObjectType, Method method) {

        List<ResolvedType> implementedInterfaces = tr.resolve(sqlObjectType).getImplementedInterfaces();
        for (ResolvedType implementedInterface : implementedInterfaces) {
            if (!implementedInterface.getTypeParameters().isEmpty()) {
                return implementedInterface.getTypeParameters().get(0).getErasedType();
            }
        }
        Annotation[][] parameterAnnotationAll = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotationAll.length; i++) {
            Annotation[] parameterAnnotations = parameterAnnotationAll[i];
            for (Annotation parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation.annotationType() == BindBean.class) {
                    return method.getParameterTypes()[i];
                }
            }
        }
        throw new IllegalArgumentException("Method does not has a BindBean annotation");
    }
}
