package github.alecsio.mmceaddons.common.hatch;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to yeet some boilerplate code from the component definitions. See {@link BaseComponent}
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresMod {
    String value();
}
