package guru.sfg.brewery.security.perms.orders;

import guru.sfg.brewery.constants.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.ORDER_PICKUP + "') " +
        "OR hasAuthority('" + Permissions.CUSTOMER_ORDER_PICKUP + "') " +
        "AND @beerOrderAuthenticationManager.customerIdMatcher(authentication, #customerId)")
public @interface BeerOrderPickupPermission {
}