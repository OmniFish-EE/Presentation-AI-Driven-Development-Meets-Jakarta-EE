package com.example.service;

import com.example.dto.PersonRequest;
import com.example.entity.Person;
import com.example.entity.Group;
import com.example.repository.PersonRepository;
import com.example.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService implements BaseService<Person, Long, PersonRequest> {

    private final PersonRepository personRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, GroupRepository groupRepository) {
        this.personRepository = personRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    @Transactional
    public Person create(PersonRequest dto) {
        Person person = new Person();
        person.setFirstName(dto.getFirstName().trim());
        person.setLastName(dto.getLastName().trim());
        
        if (dto.getGroupId() != null) {
            Group group = findGroupByIdOrThrow(dto.getGroupId());
            person.setGroup(group);
        }
        
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person update(Long id, PersonRequest dto) {
        Person person = findByIdOrThrow(id);
        
        person.setFirstName(dto.getFirstName().trim());
        person.setLastName(dto.getLastName().trim());
        
        if (dto.getGroupId() != null) {
            Group group = findGroupByIdOrThrow(dto.getGroupId());
            person.setGroup(group);
        } else {
            person.setGroup(null);
        }
        
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("Person not found");
        }
        personRepository.deleteById(id);
    }

    private Person findByIdOrThrow(Long id) {
        return personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));
    }

    private Group findGroupByIdOrThrow(Long groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }
}
