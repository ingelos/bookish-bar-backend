package com.bookish.bar.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;


@Component
public class SecurityUtils {

    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public SecurityUtils(JwtUtil jwtUtil, HttpServletRequest request) {
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    public Long getAuthenticatedUserId() throws AccessDeniedException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);

        return jwtUtil.extractId(token);
    }


    public void assertUserIsOwner(Long id) throws AccessDeniedException {
        Long authId = getAuthenticatedUserId();
        if (!authId.equals(id)) {
            throw new AccessDeniedException("You don't have permission to access this resource");
        }
    }


}
