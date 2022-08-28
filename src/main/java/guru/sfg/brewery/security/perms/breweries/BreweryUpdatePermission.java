package guru.sfg.brewery.security.perms.breweries;

import guru.sfg.brewery.constants.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.BREWERY_UPDATE + "')")
public @interface BreweryUpdatePermission {
}
