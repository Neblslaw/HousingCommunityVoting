package pl.lodz.p.it.mateusz.gielczynski.logger;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MethodCallLogged {
}

