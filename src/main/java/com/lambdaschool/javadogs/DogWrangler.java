package com.lambdaschool.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class DogWrangler
{
    private final DogCatalog dogCatalog;
    private final DogResourceAssembler assembler;

    public DogWrangler(DogCatalog dogCatalog, DogResourceAssembler assembler)
    {
        this.dogCatalog = dogCatalog;
        this.assembler = assembler;
    }

    // RequestMapping(method = RequestMethod.GET)
    @GetMapping("/dogs")
    public Resources<Resource<Dog>> all()
    {
        List<Resource<Dog>> dogs = dogCatalog.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogWrangler.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/{id}") // /dogs/4
    public Resource<Dog> findOne(@PathVariable Long id)
    {
        Dog foundDog = dogCatalog.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        return assembler.toResource(foundDog);
    }

    @PutMapping("/dogs/{id}")
    public ResponseEntity<?> replaceDog(@RequestBody Dog newDog, @PathVariable Long id)
            throws URISyntaxException
    {
        Dog updatedDog = dogCatalog.findById(id)
                .map(dog ->
                {
                    dog.setBreed(newDog.getBreed());
                    dog.setWeight(newDog.getWeight());
                    dog.setApartment(newDog.isApartment());
                    return dogCatalog.save(dog);
                })
                .orElseGet(() ->
                {
                    newDog.setId(id);
                    return dogCatalog.save(newDog);
                });

        Resource<Dog> resource = assembler.toResource(updatedDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PostMapping("/dogs/")
    public ResponseEntity<?> addDog(@RequestBody Dog newDog)
            throws URISyntaxException
    {
        dogCatalog.save(newDog);
        Dog updatedDog = dogCatalog.save(newDog);

        Resource<Dog> resource = assembler.toResource(updatedDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id)
    {
        dogCatalog.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}