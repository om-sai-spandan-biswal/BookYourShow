package com.projects.BookYourShow.backend.auth.service;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
    void attachResponseCookie(HttpServletResponse response, String refreshToken);
    void clearResponseCookie(HttpServletResponse response);
    void addNoStoreHeaders(HttpServletResponse response);

}
