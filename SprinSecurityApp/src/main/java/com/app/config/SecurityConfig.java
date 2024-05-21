package com.app.config;

import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity //habilita la seguridad web
@EnableMethodSecurity //utiliza anotaciones propias de springsecurity
public class SecurityConfig {
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())//vulnerabilidad web aparece en formularios, con aplicaciones rest no requiere, pero si usamos mvc si se necesita. un atacante toma sesion de un usuario logueado. basada en tokens que se guardan en las cookies
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//para aplicaciones web vamos a trabajar sin estado, y es que cuando alguien se loguea y va guardando la sesion en la memoria, en este caso no se guarda la sesion en memoria y va depender de la expiracion de token.
                .authorizeHttpRequests(http -> {
                    //configurar los endpoints publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/hello").permitAll(); //si le hace peticion a este endpoint no necesita autorizacion

                    //configuraar los endpoints privados
                    http.requestMatchers(HttpMethod.GET,"/auth/hello-secured").hasAuthority("READ"); //en este caso los que tenga esta autorizacion de lectura pueden acceder

                    //configurar el resto de enpoints - NO ESPECIFICADOS
                    http.anyRequest().denyAll();//rechazatodo lo que no se especifique, el autehtiqued es mas permisivo
                })
                .build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())//vulnerabilidad web aparece en formularios, con aplicaciones rest no requiere, pero si usamos mvc si se necesita. un atacante toma sesion de un usuario logueado. basada en tokens que se guardan en las cookies
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//para aplicaciones web vamos a trabajar sin estado, y es que cuando alguien se loguea y va guardando la sesion en la memoria, en este caso no se guarda la sesion en memoria y va depender de la expiracion de token.
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        List <UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("felipe")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ","CREATE")
                .build());

        userDetailsList.add(User.withUsername("juan")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();//solo se usa para pruebas.
    }

}
