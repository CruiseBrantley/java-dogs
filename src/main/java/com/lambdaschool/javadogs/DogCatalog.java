package com.lambdaschool.javadogs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DogCatalog extends JpaRepository<Dog, Long>
{
}

