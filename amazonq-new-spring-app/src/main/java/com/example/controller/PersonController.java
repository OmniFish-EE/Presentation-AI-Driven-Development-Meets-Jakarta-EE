package com.example.controller;

import com.example.dto.PersonRequest;
import com.example.entity.Person;
import com.example.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public List<Person> getAllPeople() {
        return personService.findAll();
    }

    @PostMapping("/people")
    public Person createPerson(@Valid @RequestBody PersonRequest dto) {
        return personService.create(dto);
    }

    @PutMapping("/people/{id}")
    public Person updatePerson(@PathVariable Long id, @Valid @RequestBody PersonRequest dto) {
        return personService.update(id, dto);
    }

    @DeleteMapping("/people/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.delete(id);
    }
}
