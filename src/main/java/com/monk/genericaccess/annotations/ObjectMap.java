package com.monk.genericaccess.annotations;


import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectMap {
    String table() default "";
    String key() default  "";
}
