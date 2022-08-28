package guru.sfg.brewery.security.perms.customers;

import guru.sfg.brewery.constants.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.CUSTOMER_DELETE + "')")
public @interface CustomerDeletePermission {
}
