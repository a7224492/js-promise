package com.kodgames.corgi.core.net.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionAnnotation
{
	@SuppressWarnings("rawtypes")
	Class messageClass() default void.class;
	@SuppressWarnings("rawtypes")
	Class actionClass();
	@SuppressWarnings("rawtypes")
	Class serviceClass();
}