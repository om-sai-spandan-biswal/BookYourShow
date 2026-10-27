package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.properties.JwtCookieProperties;
import com.projects.BookYourShow.backend.auth.properties.JwtProperties;
import com.projects.BookYourShow.backend.shared.properties.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.junit.jupiter.api.Assertions.*;

class CookieServiceImplTest {
    private final CookieServiceImpl service = new CookieServiceImpl(
        new JwtCookieProperties("refresh",true,true,"Strict"),
        new JwtProperties("unused",600,3600,"test"), new AppProperties("https://example.com","example.com"));
    @Test void attachesSecureHttpOnlyCookie() {
        var response = new MockHttpServletResponse(); service.attachResponseCookie(response,"token");
        String cookie = response.getHeader("Set-Cookie"); assertNotNull(cookie);
        for (String attribute : new String[]{"refresh=token","Secure","HttpOnly","SameSite=Strict","Path=/","Domain=example.com","Max-Age=3600"}) {
            assertTrue(cookie.contains(attribute),cookie);
        }
    }
    @Test void clearReplacesCookieValue() {
        var response = new MockHttpServletResponse(); service.clearResponseCookie(response);
        assertTrue(response.getHeader("Set-Cookie").startsWith("refresh=;"));
    }
    @Test void addsCachePreventionHeaders() {
        var response = new MockHttpServletResponse(); service.addNoStoreHeaders(response);
        assertEquals("no-store",response.getHeader("Cache-Control")); assertEquals("no-cache",response.getHeader("Pragma"));
    }
}
