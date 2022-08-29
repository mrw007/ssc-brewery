package guru.sfg.brewery.security.perms.orders;

import guru.sfg.brewery.constants.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('" + Permissions.ORDER_READ + "', '" + Permissions.CUSTOMER_ORDER_READ + "')")
public @interface BeerOrderReadPermissionV2 {
}
