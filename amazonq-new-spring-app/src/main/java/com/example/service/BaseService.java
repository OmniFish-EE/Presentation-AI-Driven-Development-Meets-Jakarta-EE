package com.example.service;

import java.util.List;

/**
 * Base service interface defining common CRUD operations.
 * Follows Interface Segregation Principle by providing minimal contract.
 */
public interface BaseService<T, ID, DTO> {
    
    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Create a new entity
     * @param dto Data transfer object containing entity data
     * @return Created entity
     */
    T create(DTO dto);
    
    /**
     * Update an existing entity
     * @param id Entity identifier
     * @param dto Data transfer object containing updated data
     * @return Updated entity
     */
    T update(ID id, DTO dto);
    
    /**
     * Delete an entity by ID
     * @param id Entity identifier
     */
    void delete(ID id);
}
