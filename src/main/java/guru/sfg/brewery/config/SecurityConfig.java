package guru.sfg.brewery.config;

import guru.sfg.brewery.security.CustomPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
//        return  NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return new StandardPasswordEncoder();
//        return new BCryptPasswordEncoder();
        return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/", "/webjars/**", "/resources/**", "/login").permitAll()
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("spring")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
//                .password("{noop}spring")
//                .password("spring")
                .password("{bcrypt}$2a$10$.ULEZM7Wz93RFZvA4u9ymuyeWmlF1zUz3TI21v3N//GQ12aIDRHjO")
                .roles("ADMIN")
                .and()
                .withUser("user")
//                .password("{noop}password")
//                .password("password") //noOp
//                .password("{SSHA}Y/M5fqeUqHVXbm5nMo+u1BN+3Fq+cw7D3jaxuw==") // LDAP
                .password("{sha256}92aa9949b438050c1ee17cee5c0d8caff541765606670f40b0d18f5271d26ee2655088424112bf69") // SHA-256
//                .password("$2a$10$MPphR2XwnvqmVWutmN4PXu.OZkboKRRIBTCJ4RetSjnSBpStlYzxG") // BCrypt
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{ldap}{SSHA}GMqqQ6oIleVEmlQHedJAdt40QhvydaihNbxDvA==")
                .roles("USER");
    }
}
