package org.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.stream.Collectors;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final LoggedUsersRegistry loggedUsersRegistry;

    @Autowired
    public SecurityConfig(
            @Qualifier(value = "userDetailsServiceImpl") UserDetailsService userDetailsService,
            @Lazy LoggedUsersRegistry loggedUsersRegistry) {
        this.userDetailsService = userDetailsService;
        this.loggedUsersRegistry = loggedUsersRegistry;
    }

    public SecurityConfig(boolean disableDefaults, UserDetailsService userDetailsService, LoggedUsersRegistry loggedUsersRegistry) {
        super(disableDefaults);
        this.userDetailsService = userDetailsService;
        this.loggedUsersRegistry = loggedUsersRegistry;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .authorizeRequests()
                .antMatchers("/admin**", "/admin/**").hasRole("ADMIN")
                .antMatchers("/fm**", "/fm/**").hasRole("USER")
                .antMatchers("/footer", "/changeFormation", "/formation", "/loggedUsers", "/session-expired", "/app/**", "/", "/register**", "/login", "/home", "/confirm/**", "/matches/scheduled", "/logout**").permitAll()
                .antMatchers("/changePassword", "/sendLinkToChangePassword", "/css/**", "/js/**", "/images/**", "/webjars/**", "/audio/**", "/forgotPassword", "/sendPassword").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("nick")
                .passwordParameter("pass")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error")

                .and()
                .logout()

                .invalidateHttpSession(true)
                .clearAuthentication(true)

                .logoutSuccessUrl("/logout?logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .httpBasic()

                .and()
                .rememberMe().rememberMeParameter("remember-param")
                .and()

                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());
    }


    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {

            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
//            Set<String> strings = updateLoggedUsers();
            response.sendRedirect("/logout?logout");
        };
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {

            loggedUsersRegistry.updateLoggedUsers();

            boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ADMIN");
            response.sendRedirect(isAdmin ? "/admin" : "/userPage");

        };
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, exception) -> {
            httpServletResponse.sendRedirect("/accessDenied");
            httpServletResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            httpServletResponse.addHeader("REQUESTED-URL", httpServletRequest.getRequestURL().toString());
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


//    @Cacheable(value = "loggedUsers")
//    public Set<String> getLoggedUsers() {
//
//        return sessionRegistry.getAllPrincipals()
//                .stream()
//                .filter(principal -> principal instanceof UserDetails)
//                .map(UserDetails.class::cast)
//                .map(UserDetails::getUsername)
//                .collect(Collectors.toSet());
//    }
//
//    @CachePut(value = "loggedUsers")
//    public Set<String> updateLoggedUsers() {
//
//        return sessionRegistry.getAllPrincipals()
//                .stream()
//                .filter(principal -> principal instanceof UserDetails)
//                .map(UserDetails.class::cast)
//                .map(UserDetails::getUsername)
//                .collect(Collectors.toSet());
//    }
}
