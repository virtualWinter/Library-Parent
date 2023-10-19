package me.vwinter.library.menu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuMeta {

    /**
     * Returns the aliases for this menu.
     */
    String[] aliases();

    /**
     * Returns the title for this menu.
     */
    String title();

    /**
     * Returns the amount of rows for this menu.
     */
    int rows() default 3;

}
