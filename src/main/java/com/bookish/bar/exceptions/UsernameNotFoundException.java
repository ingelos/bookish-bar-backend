package com.bookish.bar.exceptions;

import java.io.Serial;

public class UsernameNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;
    public UsernameNotFoundException(String username) {
        super("No user found with username " + username);
    }
}
