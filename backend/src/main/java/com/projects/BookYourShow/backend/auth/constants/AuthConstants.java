package com.projects.BookYourShow.backend.auth.constants;

public class AuthConstants {
    public static final String[] AUTH_PUBLIC_URLS = {
            "/auth/**",
            "/movies/**",
            "/payments/webhook/**",

            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    } ;

    public static final String[] AUTH_USER_URLS = {
            "/bokings/**",
    } ;

    public static final String[] AUTH_ADMIN_URLS = {
            "/admin/**",
            "/book"
    } ;

}
