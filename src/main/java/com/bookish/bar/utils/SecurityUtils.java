package com.bookish.bar.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;

public class SecurityUtils {

    public static boolean isAuthenticatedUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getName().equals(username);
    }

    public static void assertAuthenticatedUser(String username) throws AccessDeniedException {
        if (!isAuthenticatedUser(username)) {
            throw new AccessDeniedException("Unauthorized access attempt");
        }
    }

}
