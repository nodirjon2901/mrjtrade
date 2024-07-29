package org.example.mrj.exception;

public class NoUniqueNameException extends RuntimeException
{
    public NoUniqueNameException(String lang)
    {
        super(lang);
    }
}
