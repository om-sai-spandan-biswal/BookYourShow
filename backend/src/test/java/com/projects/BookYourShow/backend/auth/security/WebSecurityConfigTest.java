package com.projects.BookYourShow.backend.auth.security;

import com.projects.BookYourShow.backend.auth.service.JwtService;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WebSecurityConfigTest {
    private AnnotationConfigWebApplicationContext context;
    private MockMvc mvc;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(TestConfig.class);
        context.refresh();
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        jwtService = context.getBean(JwtService.class);
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void anonymousUsersCanBrowseMoviesAndShowSeats() throws Exception {
        mvc.perform(get("/movies/search")).andExpect(status().isOk());
        mvc.perform(get("/shows/1")).andExpect(status().isOk());
    }

    @Test
    void anonymousUsersCannotBookCancelOrAdminister() throws Exception {
        mvc.perform(post("/book")).andExpect(status().isUnauthorized());
        mvc.perform(post("/book/1/cancel")).andExpect(status().isUnauthorized());
        mvc.perform(get("/admin/shows/1")).andExpect(status().isUnauthorized());
        mvc.perform(post("/shows/1")).andExpect(status().isUnauthorized());
    }

    @Test
    void usersCanBookAndCancelButCannotAdminister() throws Exception {
        mvc.perform(post("/book").with(user("customer").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(post("/book/1/cancel").with(user("customer").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(post("/admin/shows").with(user("customer").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminsCanStillBookAndAdminister() throws Exception {
        mvc.perform(post("/book").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(post("/admin/shows").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void authenticatedPrincipalWithoutBookingRoleCannotBook() throws Exception {
        mvc.perform(post("/book").with(user("other").roles("OTHER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidJwtReturnsUnauthorized() throws Exception {
        when(jwtService.isAccessToken("invalid")).thenThrow(new JwtException("Invalid signature"));

        mvc.perform(post("/book").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTokenCannotAuthenticateBookingRequest() throws Exception {
        when(jwtService.isAccessToken("refresh")).thenReturn(false);

        mvc.perform(post("/book").header("Authorization", "Bearer refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validUserJwtCanBookButDisabledUserCannot() throws Exception {
        User customer = new User();
        customer.setEmail("customer@example.com");
        customer.setEnabled(true);
        customer.setRoles(Set.of(new Role(UserRole.USER)));
        when(jwtService.isAccessToken("valid")).thenReturn(true);
        when(jwtService.extractUsername("valid")).thenReturn(customer.getEmail());
        when(context.getBean(SecurityUserDetailsService.class).loadUserByUsername(customer.getEmail()))
                .thenReturn(new SecurityUserDetails(customer));

        mvc.perform(post("/book").header("Authorization", "Bearer valid"))
                .andExpect(status().isOk());

        customer.setEnabled(false);
        mvc.perform(post("/book").header("Authorization", "Bearer valid"))
                .andExpect(status().isUnauthorized());
    }

    @Configuration
    @EnableWebMvc
    @Import({WebSecurityConfig.class, JwtAuthFilter.class,
            JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class, ProbeController.class})
    static class TestConfig {
        @Bean
        JwtService jwtService() {
            return mock(JwtService.class);
        }

        @Bean
        SecurityUserDetailsService securityUserDetailsService() {
            return mock(SecurityUserDetailsService.class);
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    // Exercises the real security filter chain without loading a database or domain services.
    @RestController
    static class ProbeController {
        @RequestMapping("/**")
        String ok() {
            return "ok";
        }
    }
}
