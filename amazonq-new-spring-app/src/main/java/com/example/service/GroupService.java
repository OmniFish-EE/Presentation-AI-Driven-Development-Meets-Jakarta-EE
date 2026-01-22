package com.example.service;

import com.example.dto.GroupRequest;
import com.example.entity.Group;
import com.example.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GroupService implements BaseService<Group, Long, GroupRequest> {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    @Transactional
    public Group create(GroupRequest dto) {
        try {
            return groupRepository.save(new Group(dto.getName().trim()));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Group name already exists");
        }
    }

    @Override
    @Transactional
    public Group update(Long id, GroupRequest dto) {
        Group group = findByIdOrThrow(id);
        
        try {
            group.setName(dto.getName().trim());
            return groupRepository.save(group);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Group name already exists");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Group group = findByIdOrThrow(id);
        
        if (group.getPeople() != null && !group.getPeople().isEmpty()) {
            throw new IllegalStateException("Cannot delete group with assigned people");
        }
        
        groupRepository.deleteById(id);
    }

    private Group findByIdOrThrow(Long id) {
        return groupRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }
}
