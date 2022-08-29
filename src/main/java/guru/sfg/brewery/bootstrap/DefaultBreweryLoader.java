/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.constants.Permissions;
import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    public static final String DUNEDIN_DISTRIBUTING = "Dunedin Distributing";
    public static final String KEY_WEST_DISTRIBUTORS = "Key West Distributors";
    public static final String STPETE_USER = "stpete";
    public static final String DUNEDIN_USER = "dunedin";
    public static final String KEYWEST_USER = "keywest";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        loadSecurityData();
        loadBreweryData();
        loadTastingRoomData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        Optional<Role> customerRole = Optional.of(roleRepository.findByRoleName("CUSTOMER").orElseThrow());

        //create customers
        Customer stPeteCustomer = customerRepository.save(Customer.builder()
                .customerName(ST_PETE_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer dunedinCustomer = customerRepository.save(Customer.builder()
                .customerName(DUNEDIN_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer keyWestCustomer = customerRepository.save(Customer.builder()
                .customerName(KEY_WEST_DISTRIBUTORS)
                .apiKey(UUID.randomUUID())
                .build());

        //create users
        User stPeteUser = userRepository.save(User.builder().username(STPETE_USER)
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(customerRole.get()).build());

        User dunedinUser = userRepository.save(User.builder().username(DUNEDIN_USER)
                .password(passwordEncoder.encode("password"))
                .customer(dunedinCustomer)
                .role(customerRole.get()).build());

        User keywest = userRepository.save(User.builder().username(KEYWEST_USER)
                .password(passwordEncoder.encode("password"))
                .customer(keyWestCustomer)
                .role(customerRole.get()).build());

        //create orders
        createOrder(stPeteCustomer);
        createOrder(dunedinCustomer);
        createOrder(keyWestCustomer);

        log.debug("Orders Loaded: " + beerOrderRepository.count());
    }

    private BeerOrder createOrder(Customer customer) {
        return beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
    }

    private void loadTastingRoomData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }

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

        // Beer Order auths
        Authority createOrder = authorityRepository.save(Authority.builder().permission(Permissions.ORDER_CREATE).build());
        Authority updateOrder = authorityRepository.save(Authority.builder().permission(Permissions.ORDER_UPDATE).build());
        Authority readOrder = authorityRepository.save(Authority.builder().permission(Permissions.ORDER_READ).build());
        Authority deleteOrder = authorityRepository.save(Authority.builder().permission(Permissions.ORDER_DELETE).build());
        Authority pickupOrder = authorityRepository.save(Authority.builder().permission(Permissions.ORDER_PICKUP).build());
        Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_ORDER_CREATE).build());
        Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_ORDER_UPDATE).build());
        Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_ORDER_READ).build());
        Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_ORDER_DELETE).build());
        Authority pickupOrderCustomer = authorityRepository.save(Authority.builder().permission(Permissions.CUSTOMER_ORDER_PICKUP).build());

        // Roles
        Role adminRole = roleRepository.save(Role.builder().roleName("ADMIN").build());
        Role userRole = roleRepository.save(Role.builder().roleName("USER").build());
        Role customerRole = roleRepository.save(Role.builder().roleName("CUSTOMER").build());

        adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer,
                createCustomer, readCustomer, listCustomer, updateCustomer, deleteCustomer,
                createBrewery, readBrewery, listBrewery, updateBrewery, deleteBrewery,
                createOrder, updateOrder, readOrder, deleteOrder, pickupOrder));

        customerRole.setAuthorities(Set.of(readBeer,
                readCustomer, listCustomer,
                readBrewery, listBrewery,
                createOrderCustomer, updateOrderCustomer, readOrderCustomer, deleteOrderCustomer, pickupOrderCustomer));

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
}
