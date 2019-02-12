package com.lambdaschool.javadogs;

public class DogNotFoundException extends RuntimeException
{
    public DogNotFoundException(Long breed)
    {
        super ("Could not find dog " + breed);
    }
}