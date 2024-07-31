package org.example.mrj.security;

public class AuthorizationFailedException extends RuntimeException
{
    public AuthorizationFailedException(String message)
    {
        super(message);
    }
}
