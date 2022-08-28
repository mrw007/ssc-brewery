package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.constants.Permissions;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadSecurityData() {

        // Beer auths
        Authority createBeer = authorityRepository.save(Authority.builder().permission(Permissions.BEER_CREATE).build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission(Permissions.BEER_UPDATE).build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission(Permissions.BEER_READ).build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission(Permissions.BEER_DELETE).build());

        // Customer auths
        Authority createCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_CREATE).build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_UPDATE).build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_READ).build());
        Authority listCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_LIST).build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_DELETE).build());

        // Brewery auths
        Authority createBrewery = authorityRepository.save(Authority.builder().permission(Permissions.BREWERY_CREATE).build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission(Permissions.BREWERY_UPDATE).build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission(Permissions.BREWERY_READ).build());
        Authority listBrewery = authorityRepository.save(Authority.builder().permission(Permissions.BREWERY_LIST).build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission(Permissions.BREWERY_DELETE).build());

        // Roles
        Role adminRole = roleRepository.save(Role.builder().roleName("ADMIN").build());
        Role userRole = roleRepository.save(Role.builder().roleName("USER").build());
        Role customerRole = roleRepository.save(Role.builder().roleName("CUSTOMER").build());

        adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer,
                createCustomer, readCustomer, listCustomer, updateCustomer, deleteCustomer,
                createBrewery, readBrewery, listBrewery, updateBrewery, deleteBrewery));

        customerRole.setAuthorities(Set.of(readBeer, readCustomer, listCustomer, readBrewery, listBrewery));

        userRole.setAuthorities(Set.of(readBeer, readCustomer, readBrewery));

        roleRepository.saveAll(Arrays.asList(adminRole, userRole, customerRole));

        User spring = userRepository.save(User.builder()
                .username("spring")
                .password(passwordEncoder.encode("spring"))
                .build());

        User user = userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .build());

        User scott = userRepository.save(User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .build());

        spring.setRoles(Set.of(adminRole));
        user.setRoles(Set.of(userRole));
        scott.setRoles(Set.of(customerRole));

        userRepository.saveAll(Arrays.asList(spring, scott, user));

        log.debug("Users Loaded: " + userRepository.count());
    }

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }


}