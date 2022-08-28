package guru.sfg.brewery.security.perms.beers;

import guru.sfg.brewery.constants.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.BEER_UPDATE + "')")
public @interface BeerUpdatePermission {
}
