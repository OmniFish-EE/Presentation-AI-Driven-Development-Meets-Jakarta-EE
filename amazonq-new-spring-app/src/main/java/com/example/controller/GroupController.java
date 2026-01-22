package com.example.controller;

import com.example.dto.GroupRequest;
import com.example.entity.Group;
import com.example.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        return groupService.findAll();
    }

    @PostMapping("/groups")
    public Group createGroup(@Valid @RequestBody GroupRequest dto) {
        return groupService.create(dto);
    }

    @PutMapping("/groups/{id}")
    public Group updateGroup(@PathVariable Long id, @Valid @RequestBody GroupRequest dto) {
        return groupService.update(id, dto);
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
    }
}
